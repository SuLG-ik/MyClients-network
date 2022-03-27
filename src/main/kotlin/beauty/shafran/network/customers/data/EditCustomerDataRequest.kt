package beauty.shafran.network.customers.data

import kotlinx.serialization.Serializable

@Serializable
data class EditCustomerRequest(
    val customerId: String,
    val data: CustomerData,
)


@Serializable
data class EditCustomerResponse(
    val customer: Customer.ActivatedCustomer,
)
