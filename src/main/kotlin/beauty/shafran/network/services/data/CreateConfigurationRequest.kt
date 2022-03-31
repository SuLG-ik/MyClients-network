package beauty.shafran.network.services.data

import beauty.shafran.network.validation.ObjectIdParameter
import com.arkivanov.essenty.parcelable.Parcelable
import com.arkivanov.essenty.parcelable.Parcelize
import jakarta.validation.Valid
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.Size
import kotlinx.serialization.Serializable

@Parcelize
@Serializable
data class CreateConfigurationRequest(
    @field:ObjectIdParameter
    val serviceId: String,
    @field:Valid
    val data: CreateConfigurationRequestData,
) : Parcelable

fun CreateConfigurationRequest.trim(): CreateConfigurationRequest {
    return copy(data = data.trim())
}

@Parcelize
@Serializable
data class CreateConfigurationRequestData(
    @field:Size(min = 2)
    val title: String,
    val description: String,
    @field: Min(0)
    val cost: Int,
    @field: Min(1)
    val amount: Int,
) : Parcelable

fun CreateConfigurationRequestData.trim(): CreateConfigurationRequestData {
    return copy(title = title.trim(), description = description.trim())
}


@Parcelize
@Serializable
data class CreateConfigurationResponse(
    val service: Service,
) : Parcelable

