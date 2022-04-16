package beauty.shafran.network.auth.entity

import beauty.shafran.network.assets.entity.AssetEntity
import beauty.shafran.network.utils.MetaEntity
import kotlinx.serialization.Contextual
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.litote.kmongo.Id
import org.litote.kmongo.newId


@Serializable
class BusinessEntity(
    val data: BusinessDataEntity,
    val meta: MetaEntity = MetaEntity(),
    @Contextual
    @SerialName("_id")
    val id: Id<BusinessEntity> = newId(),
)

@Serializable
class BusinessDataEntity(
    val title: String,
    val description: String,
    val icon: AssetEntity? = null,
)

