package beauty.shafran.network.assets.entity

import beauty.shafran.network.assets.data.AssetType
import kotlinx.serialization.Contextual
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.litote.kmongo.Id
import org.litote.kmongo.newId


@Serializable
data class AssetEntity(
    val type: AssetType,
    val hash: String,
    @Contextual
    @SerialName("_id")
    val id: Id<AssetEntity> = newId(),
)