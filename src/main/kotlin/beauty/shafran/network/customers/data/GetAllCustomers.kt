package beauty.shafran.network.customers.data

import kotlinx.serialization.Serializable

@Serializable
data class GetAllCustomersRequest(
    val offset: Int = 30,
    val page: Int = 0,
    val companyId: String,
)
@Serializable
data class GetAllCustomersResponse(
    val customers: List<Customer>,
    val offset: Int,
    val page: Int,
)

@Serializable
data class GetCustomerByTokenRequest(
    val token: String,
)


@Serializable
data class GetCustomerByIdResponse(
    val cardToken: String,
    val customer: Customer,
)

@Serializable
data class GetCustomerByTokenResponse(
    val cardToken: String,
    val customer: Customer,
)
