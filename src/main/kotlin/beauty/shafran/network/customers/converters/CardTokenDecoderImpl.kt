package beauty.shafran.network.customers.converters

import beauty.shafran.network.customers.exceptions.IllegalCardToken
import io.ktor.util.*


class CardTokenDecoderImpl : CardTokenDecoder {

    private val decoders = mapOf<String, CardTokenDecoder>("0" to CardTokenDecoder0())

    @OptIn(InternalAPI::class)
    override fun decodeTokenToId(token: String): String {
        try {
            val decodedToken = token.decodeBase64String()
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