package beauty.shafran.network.cards.data

import beauty.shafran.network.customers.data.CustomerId
import kotlinx.serialization.Serializable

@Serializable
data class GetCardByCustomerIdRequest(
    val customerId: CustomerId,
)


@Serializable
data class GetCardByCustomerIdResponse(
    val card: Card,
)