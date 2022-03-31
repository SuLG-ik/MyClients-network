package beauty.shafran.network.customers.data

import beauty.shafran.network.validation.ObjectIdParameter
import jakarta.validation.Valid
import kotlinx.serialization.Serializable

@Serializable
data class EditCustomerRequest(
    @field:ObjectIdParameter
    val customerId: String,
    @field:Valid
    val data: CustomerData,
)


fun EditCustomerRequest.trim(): EditCustomerRequest {
    return copy(data = data.trim())
}

@Serializable
data class EditCustomerResponse(
    val customer: Customer.ActivatedCustomer,
)
