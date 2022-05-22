package beauty.shafran.network.assets

import beauty.shafran.network.assets.converter.AssetsConfig
import io.ktor.server.config.*
import org.koin.core.annotation.Module
import org.koin.core.annotation.Single

@Module
class Assets {

    @Single
    fun config(config: ApplicationConfig): AssetsConfig {
        System.getenv()
        return AssetsConfig(
            url = config.tryGetString("ktor.feature.assets.publicUrl")!!
        )
    }

}