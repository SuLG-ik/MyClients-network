package beauty.shafran.network.datetime

import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val DatetimeModule = module {
    factoryOf(::clockBean)
    factoryOf(::timezoneBean)
}

fun clockBean(): Clock {
    return Clock.System
}

fun timezoneBean(): TimeZone {
    return TimeZone.UTC
}