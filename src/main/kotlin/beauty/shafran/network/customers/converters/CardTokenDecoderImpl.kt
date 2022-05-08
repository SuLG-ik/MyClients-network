package beauty.shafran.network.customers.converters

import beauty.shafran.IllegalCardToken
import org.koin.core.annotation.Single
import java.util.*


@Single
class CardTokenDecoderImpl : CardTokenDecoder {

    private val decoders = mapOf<String, CardTokenDecoder>("0" to CardTokenDecoder0())

    private val decoder = Base64.getDecoder()


    override fun decodeTokenToId(token: String): String {
        try {

            val decodedToken = decoder.decode(token).decodeToString()
            val parts = decodedToken.split("$")
            val decoder = decoders[parts[0]] ?: throw IllegalArgumentException("Unknown decoder version")
            return decoder.decodeTokenToId(decodedToken)
        } catch (e: Exception) {
            throw IllegalCardToken(token)
        }
    }

}

class CardTokenDecoder0 : CardTokenDecoder {
    override fun decodeTokenToId(token: String): String {
        val parts = token.split("$")
        return parts[1]
    }
}