package beauty.shafran.network.companies.executor

import beauty.shafran.network.auth.data.AuthorizedAccount
import beauty.shafran.network.companies.data.Company
import beauty.shafran.network.companies.data.CompanyData
import beauty.shafran.network.companies.data.GetAvailableCompaniesListRequest
import beauty.shafran.network.companies.data.GetAvailableCompaniesListResponse
import beauty.shafran.network.companies.entity.CompanyEntity
import beauty.shafran.network.companies.repository.CompaniesRepository
import beauty.shafran.network.utils.Transactional
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import org.koin.core.annotation.Single

@Single
class CompaniesExecutorImpl(
    private val companiesRepository: CompaniesRepository,
    private val transactional: Transactional,
) : CompaniesExecutor {


    private fun CompanyEntity.toData(): Company {
        return Company(
            id = id,
            data = CompanyData(
                codeName = data.codename,
                title = data.title,
                creationDate = meta.creationDate
            )
        )
    }

    override suspend fun getAvailableCompaniesList(
        request: GetAvailableCompaniesListRequest,
        account: AuthorizedAccount,
    ): GetAvailableCompaniesListResponse {
        return transactional.withSuspendedTransaction {
            coroutineScope {
                val companyMembers = with(companiesRepository) { findAccountMembersForAccount(account.accountId) }
                val companies = companyMembers.map {
                    async { with(companiesRepository) { findCompanyById(it.companyId).toData() } }
                }
                GetAvailableCompaniesListResponse(
                    companies = companies.awaitAll()
                )
            }
        }
    }
}