package beauty.shafran.network

import io.ktor.server.config.*
import org.koin.core.module.dsl.createdAtStart
import org.koin.core.module.dsl.withOptions
import org.koin.dsl.module

val ExposedBeanModule = module {
    factory { getConfiguration(get()) }
    single { get<DatabaseInitializer>().initialize(get()) } withOptions {
        createdAtStart()
    }
}

private fun getConfiguration(config: ApplicationConfig): DatabaseConfiguration {
    return DatabaseConfiguration(
        url = config.tryGetString("exposed.datasource.url")!!,
        user = config.tryGetString("exposed.datasource.user")!!,
        password = config.tryGetString("exposed.datasource.password")!!,
        driver = config.tryGetString("exposed.datasource.driver")!!,
    )
}