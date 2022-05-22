package beauty.shafran.network.cards.data

import kotlinx.serialization.Serializable

@Serializable
data class CardsStorage(
    val id: CardsStorageId,
)

@Serializable
@JvmInline
value class CardsStorageId(val id: Long)