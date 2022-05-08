package beauty.shafran.network.companies.executor

import beauty.shafran.network.auth.data.AuthorizedAccount
import beauty.shafran.network.companies.data.CreateCompanyRequest
import beauty.shafran.network.companies.data.CreateCompanyResponse
import beauty.shafran.network.companies.data.GetAvailableCompaniesListRequest
import beauty.shafran.network.companies.data.GetAvailableCompaniesListResponse

interface CompaniesExecutor {

    suspend fun createCompany(request: CreateCompanyRequest, account: AuthorizedAccount): CreateCompanyResponse

    suspend fun getAvailableCompaniesList(
        request: GetAvailableCompaniesListRequest,
        account: AuthorizedAccount,
    ): GetAvailableCompaniesListResponse

}