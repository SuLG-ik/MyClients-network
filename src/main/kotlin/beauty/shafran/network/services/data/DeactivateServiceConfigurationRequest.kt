package beauty.shafran.network.services.data

import beauty.shafran.network.validation.ObjectIdParameter
import com.arkivanov.essenty.parcelable.Parcelable
import com.arkivanov.essenty.parcelable.Parcelize
import kotlinx.serialization.Serializable

@Parcelize
@Serializable
data class DeactivateServiceConfigurationRequest(
    @field:ObjectIdParameter
    val serviceId: String,
    @field:ObjectIdParameter
    val configurationId: String,
    val data: DeactivateServiceConfigurationRequestData,
) : Parcelable

fun DeactivateServiceConfigurationRequest.trim(): DeactivateServiceConfigurationRequest {
    return copy(data = data.trim())
}

private fun DeactivateServiceConfigurationRequestData.trim(): DeactivateServiceConfigurationRequestData {
    return copy(reason = reason.trim())
}

@Parcelize
@Serializable
data class DeactivateServiceConfigurationRequestData(
    val reason: String,
) : Parcelable


@Parcelize
@Serializable
data class DeactivateServiceConfigurationResponse(
    val service: Service,
) : Parcelable

