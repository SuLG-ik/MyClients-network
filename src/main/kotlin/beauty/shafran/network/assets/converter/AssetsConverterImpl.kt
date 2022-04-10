package beauty.shafran.network.assets.converter

import beauty.shafran.network.assets.data.AssetData
import beauty.shafran.network.assets.entity.AssetEntity
import org.bson.types.ObjectId
import org.litote.kmongo.newId
import org.springframework.stereotype.Service
import java.time.ZoneId
import java.time.ZonedDateTime

@Service
class AssetsConverterImpl(private val config: AssetsConfig) : AssetsConverter {

    override fun AssetEntity.toData(): AssetData {
        return AssetData(
            date = ZonedDateTime.ofInstant(ObjectId(id.toString()).date.toInstant(), ZoneId.systemDefault()),
            hash = hash,
            url = "${config.url}/$hash",
            type = type,
        )
    }

    override fun AssetData.toNewEntity(): AssetEntity {
        return AssetEntity(
            id = newId(),
            hash = hash,
            type = type,
        )
    }


}