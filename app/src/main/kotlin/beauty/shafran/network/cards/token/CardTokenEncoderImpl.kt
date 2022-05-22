package beauty.shafran.network.cards.token

import beauty.shafran.network.cards.data.Card
import beauty.shafran.network.cards.data.CardId
import beauty.shafran.network.cards.data.CardToken
import beauty.shafran.network.cards.entity.CardEntity
import beauty.shafran.network.utils.MetaData
import org.koin.core.annotation.Single
import java.util.*

@Single
class CardTokenEncoderImpl : CardTokenEncoder {

    override fun encodeTokenByCard(cardEntity: CardEntity): Card {
        val token = buildString {
            append(VERSION)
            append(SPLITTER)
            append(cardEntity.id.toString())
        }
        return Card(
            token = CardToken(Base64.getEncoder().encodeToString(token.toByteArray())),
            meta = MetaData(cardEntity.creationDate),
            id = CardId(cardEntity.id),
        )
    }

    companion object {
        private const val VERSION = "0"
        const val SPLITTER = "\$"
    }

}