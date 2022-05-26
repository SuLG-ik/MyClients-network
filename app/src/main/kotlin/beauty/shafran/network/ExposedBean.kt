package beauty.shafran.network

import beauty.shafran.network.utils.ExposedTransactional
import beauty.shafran.network.utils.Transactional
import io.ktor.server.config.*
import org.koin.core.module.dsl.createdAtStart
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.withOptions
import org.koin.dsl.bind
import org.koin.dsl.module
import org.slf4j.Logger

val ExposedBeanModule = module {
    factoryOf(::getConfiguration)
    factoryOf(::getDatabaseInitializer)
    single { get<DatabaseInitializer>().initialize(get()) } withOptions {
        createdAtStart()
    }
    factoryOf(::ExposedTransactional) bind Transactional::class
}

private fun getConfiguration(config: ApplicationConfig): DatabaseConfiguration {
    return DatabaseConfiguration(
        url = config.tryGetString("exposed.datasource.url")!!,
        user = config.tryGetString("exposed.datasource.user")!!,
        password = config.tryGetString("exposed.datasource.password")!!,
        driver = config.tryGetString("exposed.datasource.driver")!!,
    )
}

private fun getDatabaseInitializer(logger: Logger): DatabaseInitializer {
    return NativeDatabaseInitializer(logger)
}