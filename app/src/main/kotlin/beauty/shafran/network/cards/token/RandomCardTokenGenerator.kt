package beauty.shafran.network.cards.token

import beauty.shafran.network.cards.data.CardToken
import kotlin.random.Random

class RandomCardTokenGenerator(
    private val random: Random,
) : CardTokenGenerator {

    override fun generate(): CardToken {
        return CardToken(buildString(32) { repeat(32) { append(alphabet.random(random)) } })
    }

    companion object {
        private const val alphabet = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz"
    }

}