package beauty.shafran.network.customers.data

import com.arkivanov.essenty.parcelable.Parcelable
import com.arkivanov.essenty.parcelable.Parcelize
import jakarta.validation.Valid
import jakarta.validation.constraints.Min
import kotlinx.serialization.Serializable

@Parcelize
@Serializable
data class CreateEmptyCustomersRequest(
    @field:Min(1)
    val count: Int,
) : Parcelable

@Parcelize
@Serializable
data class CreateEmptyCustomersResponse(
    val cards: Map<String, Customer.InactivatedCustomer>,
) : Parcelable


@Parcelize
@Serializable
data class CreateCustomersRequest(
    @field:Valid
    val data: CustomerData,
) : Parcelable

fun CreateCustomersRequest.trim(): CreateCustomersRequest {
    return copy(data = data.trim())
}

@Parcelize
@Serializable
data class CreateCustomersResponse(
    val token: String,
    val customer: Customer.ActivatedCustomer,
) : Parcelable