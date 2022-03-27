package beauty.shafran.network.services.data

import com.arkivanov.essenty.parcelable.Parcelable
import com.arkivanov.essenty.parcelable.Parcelize
import kotlinx.serialization.Serializable

@Parcelize
@Serializable
data class GetAllServicesRequest(
    val sort: String? = null,
    val page: Int? = null
):Parcelable

@Parcelize
@Serializable
data class GetAllServicesResponse(
    val services: List<Service>,
    val page: Int? = null,
) : Parcelable