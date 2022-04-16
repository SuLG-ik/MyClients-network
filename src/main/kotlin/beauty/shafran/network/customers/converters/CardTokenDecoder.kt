package beauty.shafran.network.customers.converters

import org.springframework.stereotype.Service

interface CardTokenDecoder {

    fun decodeTokenToId(token: String): String

}