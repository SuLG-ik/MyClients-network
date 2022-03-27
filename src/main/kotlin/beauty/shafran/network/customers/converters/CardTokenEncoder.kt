package beauty.shafran.network.customers.converters

import beauty.shafran.network.customers.entity.CardEntity

interface CardTokenEncoder {

    fun encodeTokenByCard(cardEntity: CardEntity): String

}