package beauty.shafran.network.companies.executor

import beauty.shafran.network.auth.data.AuthorizedAccount
import beauty.shafran.network.companies.data.*
import beauty.shafran.network.companies.entity.CompanyEntity
import beauty.shafran.network.companies.entity.CompanyEntityData
import beauty.shafran.network.companies.repository.CompaniesRepository
import beauty.shafran.network.utils.toZonedDateTime
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import org.koin.core.annotation.Single

@Single
class CompaniesExecutorImpl(
    private val companiesRepository: CompaniesRepository,
) : CompaniesExecutor {
    override suspend fun createCompany(
        request: CreateCompanyRequest,
        account: AuthorizedAccount,
    ): CreateCompanyResponse {
        val company = companiesRepository.createCompany(
            CompanyEntityData(
                title = request.data.companyTitle,
                codeName = request.data.companyCodeName,
            ),
            account.accountId,
        )
        return CreateCompanyResponse(
            company = company.toData()
        )
    }

    private fun CompanyEntity.toData(): Company {
        return Company(
            id = CompanyId(id.toString()),
            data = CompanyData(
                codeName = data.codeName,
                title = data.title,
                creationDate = meta.creationDate.toZonedDateTime()
            )
        )
    }

    override suspend fun getAvailableCompaniesList(
        request: GetAvailableCompaniesListRequest,
        account: AuthorizedAccount,
    ): GetAvailableCompaniesListResponse {
        return coroutineScope {
            val companyMembers = companiesRepository.findAccountMembersForAccount(account.accountId)
            val companies = companyMembers.map {
                async { companiesRepository.findCompanyById(it.companyId).toData() }
            }
            GetAvailableCompaniesListResponse(
                companies = companies.awaitAll()
            )
        }
    }
}