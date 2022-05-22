package beauty.shafran.network.assets.entity

import beauty.shafran.network.assets.data.AssetType
import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable


data class AssetEntity(
    val type: AssetType,
    val hash: String,
    val date: LocalDateTime,
    val id: AssetId,
)


@JvmInline
@Serializable
value class AssetId(
    val id: Long,
)