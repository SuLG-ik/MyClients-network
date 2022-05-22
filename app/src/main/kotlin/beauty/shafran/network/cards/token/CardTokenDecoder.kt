package beauty.shafran.network.cards.token

import beauty.shafran.network.cards.data.CardToken


interface CardTokenDecoder {

    fun decodeRawToken(token: String): CardToken

}