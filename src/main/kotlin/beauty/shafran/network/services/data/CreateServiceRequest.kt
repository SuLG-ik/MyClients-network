package beauty.shafran.network.services.data

import beauty.shafran.network.validation.ObjectIdParameter
import com.arkivanov.essenty.parcelable.Parcelable
import com.arkivanov.essenty.parcelable.Parcelize
import kotlinx.serialization.Serializable
import javax.validation.Valid
import javax.validation.constraints.Size

@Parcelize
@Serializable
data class EditableServiceData(
    @field:Size(min = 2)
    val title: String,
    val description: String,
) : Parcelable

fun EditableServiceData.trim(): EditableServiceData {
    return copy(description = description.trim(), title = title.trim())
}

@Parcelize
@Serializable
data class CreateServiceRequest(
    @field:Valid
    val data: EditableServiceData,
) : Parcelable


fun CreateServiceRequest.trim(): CreateServiceRequest {
    return copy(data = data.trim())
}

@Parcelize
@Serializable
data class CreateServiceResponse(
    val service: Service,
) : Parcelable


@Parcelize
@Serializable
data class EditServiceRequest(
    @field:ObjectIdParameter
    val serviceId: String,
    @field:Valid

    val data: EditableServiceData,
) : Parcelable

@Parcelize
@Serializable
data class EditServiceResponse(
    val service: Service,
) : Parcelable