package beauty.shafran.network.companies.data

import beauty.shafran.network.accounts.data.AccountId
import beauty.shafran.network.paged.data.PagedDataRequest
import kotlinx.serialization.Serializable

@Serializable
data class GetAvailableCompaniesRequest(
    val accountId: AccountId? = null,
    val pagedData: PagedDataRequest? = null,
)

@Serializable
data class GetAvailableCompaniesResponse(
    val companies: List<Company>,
)