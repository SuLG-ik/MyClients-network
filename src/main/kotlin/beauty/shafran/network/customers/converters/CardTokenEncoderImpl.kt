package beauty.shafran.network.customers.converters

import beauty.shafran.network.customers.entity.CardEntity
import java.util.*

class CardTokenEncoderImpl : CardTokenEncoder {

    override fun encodeTokenByCard(cardEntity: CardEntity): String {
        val token = buildString {
            append(VERSION)
            append(SPLITTER)
            append(cardEntity.id.toString())
        }
        return Base64.getEncoder().encodeToString(token.toByteArray())
    }

    companion object {
        private const val VERSION = "0"
        const val SPLITTER = "\$"
    }

}