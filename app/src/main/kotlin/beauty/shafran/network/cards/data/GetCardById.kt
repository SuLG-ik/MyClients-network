package beauty.shafran.network.cards.data

import kotlinx.serialization.Serializable

@Serializable
data class GetCardByIdRequest(
    val cardId: CardId,
)

@Serializable
data class GetCardByIdResponse(
    val card: Card,
)