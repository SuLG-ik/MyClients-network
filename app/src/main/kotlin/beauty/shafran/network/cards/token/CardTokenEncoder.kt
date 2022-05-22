package beauty.shafran.network.cards.token

import beauty.shafran.network.cards.data.Card
import beauty.shafran.network.cards.entity.CardEntity

interface CardTokenEncoder {

    fun encodeTokenByCard(cardEntity: CardEntity): Card

}