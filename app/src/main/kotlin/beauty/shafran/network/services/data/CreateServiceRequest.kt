package beauty.shafran.network.services.data

import com.arkivanov.essenty.parcelable.Parcelable
import com.arkivanov.essenty.parcelable.Parcelize
import kotlinx.serialization.Serializable

@Parcelize
@Serializable
data class EditableServiceData(
    val title: String = "",
    val description: String = "",
) : Parcelable

@Parcelize
@Serializable
data class CreateServiceRequest(
    val data: EditableServiceData,
    val storageId: ServicesStorageId,
) : Parcelable

@Parcelize
@Serializable
data class EditServiceRequest(
    val serviceId: ServiceId,
    val data: EditableServiceData,
) : Parcelable


@Parcelize
@Serializable
data class CreateServiceResponse(
    val service: Service,
) : Parcelable

@Parcelize
@Serializable
data class EditServiceResponse(
    val service: Service,
) : Parcelable