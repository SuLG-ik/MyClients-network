package beauty.shafran.network.companies.data

import beauty.shafran.network.account.data.AccountId
import beauty.shafran.network.utils.PagedData
import com.arkivanov.essenty.parcelable.Parcelable
import com.arkivanov.essenty.parcelable.Parcelize
import kotlinx.serialization.Serializable

@Serializable
@Parcelize
class GetAvailableCompaniesListRequest(
    val accountId: AccountId = AccountId(0),
    val pagedData: PagedData = PagedData(0, 30),
) : Parcelable

@Serializable
@Parcelize
data class GetAvailableCompaniesListResponse(
    val companies: List<Company>,
) : Parcelable