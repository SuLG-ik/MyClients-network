package beauty.shafran.network.utils

import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module
import java.security.SecureRandom
import kotlin.random.Random
import kotlin.random.asKotlinRandom

val RandomModule = module {
    factoryOf(::randomBean)
}

private fun randomBean(): Random {
    return SecureRandom().asKotlinRandom()
}