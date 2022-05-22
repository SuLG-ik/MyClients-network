package beauty.shafran.network.cards.converter

import beauty.shafran.network.cards.data.Card
import beauty.shafran.network.cards.entity.CardEntity

interface CardsConverter {

    fun buildCard(cardEntity: CardEntity): Card

}