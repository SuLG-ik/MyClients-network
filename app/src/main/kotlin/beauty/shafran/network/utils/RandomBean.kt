package beauty.shafran.network.utils

import kotlinx.datetime.Clock
import org.koin.core.annotation.Module
import org.koin.core.annotation.Single
import java.security.SecureRandom
import kotlin.random.Random
import kotlin.random.asKotlinRandom

@Module
class RandomBean {

    @Single
    fun randomBean(): Random {
        return SecureRandom().asKotlinRandom()
    }

    @Single
    fun clockBean(): Clock {
        return Clock.System
    }

}