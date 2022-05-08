package beauty.shafran.network.companies.data

import com.arkivanov.essenty.parcelable.Parcelable
import com.arkivanov.essenty.parcelable.Parcelize
import kotlinx.serialization.Serializable

@Serializable
@Parcelize
class GetAvailableCompaniesListRequest(
    val accountId: String,
    val offset: Int = 30,
    val page: Int = 0,
) : Parcelable

@Serializable
@Parcelize
data class GetAvailableCompaniesListResponse(
    val companies: List<Company>,
) : Parcelable