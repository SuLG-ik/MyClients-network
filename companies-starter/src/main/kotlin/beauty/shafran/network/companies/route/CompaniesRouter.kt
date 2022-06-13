package beauty.shafran.network.companies.route

import beauty.shafran.network.auth.AuthorizedAccount
import beauty.shafran.network.companies.data.*


interface CompaniesRouter {

    suspend fun createCompany(
        request: CreateCompanyRequest,
        account: AuthorizedAccount,
    ): CreateCompanyResponse

    suspend fun getAvailableCompanies(
        request: GetAvailableCompaniesRequest,
        account: AuthorizedAccount,
    ): GetAvailableCompaniesResponse


    suspend fun createCompanyPlacement(
        request: CreatePlacementRequest,
        account: AuthorizedAccount,
    ): CreatePlacementResponse


}