package beauty.shafran.network

import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module
import org.slf4j.LoggerFactory

val LoggerModule = module {
    factoryOf(LoggerFactory::getILoggerFactory)
}