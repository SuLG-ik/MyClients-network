package beauty.shafran.network.assets

import beauty.shafran.network.assets.converter.AssetsConfig
import beauty.shafran.network.assets.converter.AssetsConverter
import beauty.shafran.network.assets.converter.AssetsConverterImpl
import io.ktor.server.config.*
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.bind
import org.koin.dsl.module

val AssetsModule = module {
    factoryOf(::config)
    factoryOf(::AssetsConverterImpl) bind AssetsConverter::class
}

private fun config(config: ApplicationConfig): AssetsConfig {
    return AssetsConfig(
        url = config.tryGetString("ktor.feature.assets.publicUrl")!!
    )
}