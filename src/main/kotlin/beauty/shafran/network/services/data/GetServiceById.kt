package beauty.shafran.network.services.data

import com.arkivanov.essenty.parcelable.Parcelable
import com.arkivanov.essenty.parcelable.Parcelize
import kotlinx.serialization.Serializable

@Parcelize
@Serializable
data class GetServiceByIdRequest(
    val serviceId: String,
) : Parcelable

@Parcelize
@Serializable
data class GetServiceByIdResponse(
    val service: Service?,
) : Parcelable
