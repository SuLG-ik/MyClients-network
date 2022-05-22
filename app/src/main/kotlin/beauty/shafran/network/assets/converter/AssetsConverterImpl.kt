package beauty.shafran.network.assets.converter

import beauty.shafran.network.assets.data.AssetData
import beauty.shafran.network.assets.entity.AssetEntity
import org.koin.core.annotation.Single

@Single
class AssetsConverterImpl(private val config: AssetsConfig) : AssetsConverter {

    override fun AssetEntity.toData(): AssetData {
        return AssetData(
            date = date,
            hash = hash,
            url = "${config.url}/$hash",
            type = type,
        )
    }

}