package beauty.shafran.network.cards.data

import kotlinx.serialization.Serializable

@Serializable
data class CreateCardsRequest(
    val count: Int,
    val cardsStorageId: CardsStorageId,
)

@Serializable
data class CreateCardsResponse(
    val cards: List<Card>,
)