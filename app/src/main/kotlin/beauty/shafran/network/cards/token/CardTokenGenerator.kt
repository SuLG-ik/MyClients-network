package beauty.shafran.network.cards.token

import beauty.shafran.network.cards.data.CardToken

interface CardTokenGenerator {

    fun generate(): CardToken

}