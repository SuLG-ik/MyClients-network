package beauty.shafran.network.customers.data

import kotlinx.serialization.Serializable

@Serializable
data class GetCustomerByIdRequest(
    val customerId: String,
)
