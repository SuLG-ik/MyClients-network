package beauty.shafran.network.cards.data

import beauty.shafran.network.utils.MetaData
import kotlinx.serialization.Serializable

@Serializable
@JvmInline
value class CardId(val id: Long)

@Serializable
@JvmInline
value class CardToken(val token: String)

@Serializable
data class Card(
    val token: CardToken,
    val id: CardId,
    val meta: MetaData,
)