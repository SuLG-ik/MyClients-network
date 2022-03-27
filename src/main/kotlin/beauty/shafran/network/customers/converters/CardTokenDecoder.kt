package beauty.shafran.network.customers.converters

interface CardTokenDecoder {

    fun decodeTokenToId(token: String): String

}