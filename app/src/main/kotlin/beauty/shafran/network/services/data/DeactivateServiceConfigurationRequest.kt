package beauty.shafran.network.services.data

import com.arkivanov.essenty.parcelable.Parcelable
import com.arkivanov.essenty.parcelable.Parcelize
import kotlinx.serialization.Serializable

@Parcelize
@Serializable
data class DeactivateServiceConfigurationRequest(
    val serviceId: String,
    val configurationId: String,
    val data: DeactivateServiceConfigurationRequestData,
) : Parcelable


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


