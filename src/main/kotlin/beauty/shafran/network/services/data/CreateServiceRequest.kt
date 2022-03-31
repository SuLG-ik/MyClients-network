package beauty.shafran.network.services.data

import com.arkivanov.essenty.parcelable.Parcelable
import com.arkivanov.essenty.parcelable.Parcelize
import jakarta.validation.constraints.Size
import kotlinx.serialization.Serializable


@Parcelize
@Serializable
data class CreateServiceRequest(
    @field:Size(min = 2)
    val title: String,
    val description: String,
) : Parcelable

fun CreateServiceRequest.trim(): CreateServiceRequest {
    return copy(description = description.trim(), title = title.trim())
}

@Parcelize
@Serializable
data class CreateServiceResponse(
    val service: Service,
) : Parcelable

