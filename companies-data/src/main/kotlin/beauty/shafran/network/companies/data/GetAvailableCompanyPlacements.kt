package beauty.shafran.network.companies.data

import beauty.shafran.network.accounts.data.AccountId
import kotlinx.serialization.Serializable

@Serializable
class GetAvailableCompanyPlacementsRequest(
    val companyId: CompanyId,
    val accountId: AccountId? = null,
)

@Serializable
class GetAvailableCompanyPlacementsResponse(
    val placements: List<CompanyPlacement>
)

@Serializable
class GetCompaniesPlacementsRequest(
    val companyId: CompanyId,
)

@Serializable
class GetCompaniesPlacementsResponse(
    val placements: List<CompanyPlacement>
)