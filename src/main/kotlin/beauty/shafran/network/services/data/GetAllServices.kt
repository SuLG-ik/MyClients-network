package beauty.shafran.network.services.data

import com.arkivanov.essenty.parcelable.Parcelable
import com.arkivanov.essenty.parcelable.Parcelize
import jakarta.validation.constraints.Min
import kotlinx.serialization.Serializable

@Parcelize
@Serializable
data class GetAllServicesRequest(
    @field:Min(1)
    val offset: Int = 30,
    @field:Min(0)
    val page: Int = 0,
) : Parcelable

@Parcelize
@Serializable
data class GetAllServicesResponse(
    val services: List<Service>,
    val offset: Int = 30,
    val page: Int = 0,
) : Parcelable