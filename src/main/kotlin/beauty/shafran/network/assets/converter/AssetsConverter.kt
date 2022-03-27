package beauty.shafran.network.assets.converter

import beauty.shafran.network.assets.data.AssetData
import beauty.shafran.network.assets.entity.AssetEntity

interface AssetsConverter {
    fun AssetEntity.toData(): AssetData
    fun AssetData.toNewEntity(): AssetEntity
}