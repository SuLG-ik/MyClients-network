package beauty.shafran.network.customers.data

import beauty.shafran.network.validation.ObjectIdParameter
import kotlinx.serialization.Serializable
import javax.validation.constraints.Min

@Serializable
data class GetAllCustomersRequest(
    @field:Min(1)
    val offset: Int = 30,
    @field:Min(0)
    val page: Int = 0,
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
data class GetCustomerByIdRequest(
    @field:ObjectIdParameter
    val id: String,
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
