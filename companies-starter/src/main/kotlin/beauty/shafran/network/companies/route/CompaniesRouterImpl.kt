package beauty.shafran.network.companies.route

import beauty.shafran.network.accounts.data.AccountId
import beauty.shafran.network.auth.AuthorizedAccount
import beauty.shafran.network.companies.converter.CompanyConverter
import beauty.shafran.network.companies.data.*
import beauty.shafran.network.companies.repository.CompanyPlacementRepository
import beauty.shafran.network.companies.repository.CompanyRepository
import beauty.shafran.network.database.Transactional
import beauty.shafran.network.database.invoke
import kotlinx.coroutines.awaitAll

internal class CompaniesRouterImpl(
    private val transactional: Transactional,
    private val companyRepository: CompanyRepository,
    private val converter: CompanyConverter,
    private val placementRepository: CompanyPlacementRepository,
) : CompaniesRouter {

    override suspend fun createCompany(
        request: CreateCompanyRequest,
        account: AuthorizedAccount,
    ): CreateCompanyResponse {
        return transactional {
            val (companyEntity, companyData, companyOwner) = companyRepository.createCompany(
                codename = request.codename,
                ownerId = AccountId(account.accountId),
                title = request.title ?: request.codename.value,
            )
            CreateCompanyResponse(
                company = converter.toCompany(
                    requestAccountId = AccountId(account.accountId),
                    companyEntity = companyEntity,
                    companyDataEntity = companyData,
                    companyOwnerEntity = companyOwner,
                    isMember = true,
                )
            )
        }
    }


    override suspend fun getAvailableCompanies(
        request: GetAvailableCompaniesRequest,
        account: AuthorizedAccount,
    ): GetAvailableCompaniesResponse {
        return transactional {
            val accountId = request.accountId ?: AccountId(account.accountId)
            val companies = companyRepository.getAvailableCompanies(
                accountId = accountId,
                pagedData = request.pagedData
            )
            GetAvailableCompaniesResponse(
                companies = companies.map {
                    converter.toCompany(
                        requestAccountId = AccountId(value = account.accountId),
                        companyEntity = it.first,
                        companyDataEntity = it.second,
                        companyOwnerEntity = it.third,
                        isMember = true,
                    )
                }
            )
        }
    }

    override suspend fun createCompanyPlacement(
        request: CreatePlacementRequest,
        account: AuthorizedAccount,
    ): CreatePlacementResponse {
        return transactional {
            val (placementEntity, placementData) = placementRepository.createPlacement(
                request.companyId,
                request.codename,
                request.title
            )
            CreatePlacementResponse(
                placement = converter.toCompanyPlacement(
                    placementEntity = placementEntity,
                    placementData = placementData,
                    isMember = false
                )
            )
        }
    }

    override suspend fun getAvailableCompanyPlacements(
        request: GetAvailableCompanyPlacementsRequest,
        account: AuthorizedAccount,
    ): GetAvailableCompanyPlacementsResponse {
        return transactional {
            val accountId = request.accountId ?: AccountId(account.accountId)
            val placements =
                placementRepository.getAvailablePlacements(
                    companyId = request.companyId,
                    accountId = accountId
                )
            GetAvailableCompanyPlacementsResponse(
                placements.map {
                    converter.toCompanyPlacement(
                        placementEntity = it.first,
                        placementData = it.second,
                        isMember = AccountId(account.accountId) == accountId
                    )
                }
            )
        }
    }

    override suspend fun getCompaniesPlacements(
        request: GetCompaniesPlacementsRequest,
        account: AuthorizedAccount,
    ): GetCompaniesPlacementsResponse {
        return transactional {
            val placements =
                placementRepository.getCompanyPlacementsList(
                    companyId = request.companyId,
                )
            GetCompaniesPlacementsResponse(
                placements.map {
                    transactionAsync {
                        converter.toCompanyPlacement(
                            placementEntity = it.first,
                            placementData = it.second,
                            isMember = placementRepository.isMemberOfPlacement(
                                accountId = AccountId(account.accountId),
                                placementId = CompanyPlacementId(it.first.id),
                            )
                        )
                    }
                }.awaitAll()
            )
        }
    }
}