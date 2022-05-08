package beauty.shafran.network.assets

import beauty.shafran.network.assets.converter.AssetsConfig
import org.koin.core.annotation.Module
import org.koin.core.annotation.Single

@Module
class Assets {

    @Single
    fun config(): AssetsConfig {
        return AssetsConfig()
    }

}