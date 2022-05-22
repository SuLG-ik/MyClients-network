package beauty.shafran.network.customers.data

import beauty.shafran.network.utils.PagedData
import kotlinx.serialization.Serializable

@Serializable
data class GetAllCustomersRequest(
    val paged: PagedData,
    val storageId: CustomersStorageId,
)

@Serializable
data class GetAllCustomersResponse(
    val customers: List<Customer>,
    val paged: PagedData,
)

@Serializable
data class GetCustomerByTokenRequest(
    val rawToken: String,
)


@Serializable
data class GetCustomerByIdResponse(
    val customer: Customer,
)

@Serializable
data class GetCustomerByTokenResponse(
    val customer: Customer,
)
