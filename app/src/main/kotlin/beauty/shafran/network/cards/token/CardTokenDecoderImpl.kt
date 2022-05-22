package beauty.shafran.network.cards.token

import beauty.shafran.IllegalCardToken
import beauty.shafran.network.cards.data.CardToken
import org.koin.core.annotation.Single
import java.util.*


@Single
class CardTokenDecoderImpl : CardTokenDecoder {

    private val decoders = mapOf<String, CardTokenDecoder>("0" to CardTokenDecoder0())

    private val decoder = Base64.getDecoder()


    override fun decodeRawToken(token: String): CardToken {
        try {

            val decodedToken = decoder.decode(token).decodeToString()
            val parts = decodedToken.split("$")
            val decoder = decoders[parts[0]] ?: throw IllegalArgumentException("Unknown decoder version")
            return decoder.decodeRawToken(decodedToken)
        } catch (e: Exception) {
            throw IllegalCardToken(token)
        }
    }

}

class CardTokenDecoder0 : CardTokenDecoder {
    override fun decodeRawToken(token: String): CardToken {
        val parts = token.split("$")
        return CardToken(parts[1])
    }
}