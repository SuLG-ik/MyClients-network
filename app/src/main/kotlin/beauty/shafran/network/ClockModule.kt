package beauty.shafran.network

import kotlinx.datetime.Clock
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val ClockModule = module {
    factoryOf(::clockBean)
}

fun clockBean(): Clock {
    return Clock.System
}