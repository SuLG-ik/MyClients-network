package beauty.shafran.network.services.data

import com.arkivanov.essenty.parcelable.Parcelable
import com.arkivanov.essenty.parcelable.Parcelize
import kotlinx.serialization.Serializable

@Parcelize
@Serializable
data class CreateConfigurationRequest(
    val serviceId: ServiceId,
    val data: CreateConfigurationRequestData,
) : Parcelable


@Parcelize
@Serializable
data class CreateConfigurationRequestData(
    val title: String,
    val description: String,
    val cost: Int,
    val amount: Int,
) : Parcelable


@Parcelize
@Serializable
data class CreateConfigurationResponse(
    val service: Service,
) : Parcelable

