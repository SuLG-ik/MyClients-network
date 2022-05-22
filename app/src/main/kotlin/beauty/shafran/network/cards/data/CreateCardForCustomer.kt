package beauty.shafran.network.cards.data

import beauty.shafran.network.customers.data.CustomerId

data class CreateCardForCustomerRequest(
    val customerId: CustomerId,
    val cardsStorageId: CardsStorageId,
)


data class CreateCardForCustomerResponse(
    val card: Card,
)