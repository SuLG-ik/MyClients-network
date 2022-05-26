package beauty.shafran.network.companies.executor

import beauty.shafran.network.auth.data.AuthorizedAccount
import beauty.shafran.network.companies.converter.CompanyConverter
import beauty.shafran.network.companies.data.CompanyId
import beauty.shafran.network.companies.data.GetAvailableCompaniesListRequest
import beauty.shafran.network.companies.data.GetAvailableCompaniesListResponse
import beauty.shafran.network.companies.repository.CompaniesRepository
import beauty.shafran.network.utils.Transactional
import org.slf4j.ILoggerFactory


class CompaniesExecutorImpl(
    private val companiesRepository: CompaniesRepository,
    private val converter: CompanyConverter,
    private val transactional: Transactional,
    loggerFactory: ILoggerFactory,
) : CompaniesExecutor {

    private val logger = loggerFactory.getLogger("CompaniesExecutor")

    override suspend fun getAvailableCompaniesList(
        request: GetAvailableCompaniesListRequest,
        account: AuthorizedAccount,
    ): GetAvailableCompaniesListResponse {
        return transactional.withSuspendedTransaction {
            val accountId = if (request.accountId.id != 0L) request.accountId else account.accountId
            val members =
                companiesRepository.findCompaniesForAccount(accountId = accountId, pagedData = request.pagedData)
                    .map { CompanyId(it.companyId) }
            val companiesDeferred = transactionAsync { companiesRepository.findCompaniesById(members) }
            val companiesDataDeferred = transactionAsync { companiesRepository.findCompaniesDataById(members) }
            val companies = companiesDeferred.await()
            val companiesData = companiesDataDeferred.await()
            GetAvailableCompaniesListResponse(
                companies = companies.mapNotNull {
                    val data = companiesData[CompanyId(it.id)]
                    if (data == null) {
                        logger.warn("Company was skipped because data does not exists (${it.id}:${it.codename})")
                        return@mapNotNull null
                    }
                    converter.buildCompany(it, data)
                }
            )
        }
    }
}