package beauty.shafran.network.companies.entity

import beauty.shafran.network.utils.MetaEntity
import kotlinx.serialization.Contextual
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.litote.kmongo.Id
import org.litote.kmongo.newId

@Serializable
data class CompanyEntity(
    val data: CompanyEntityData,
    val meta: MetaEntity = MetaEntity(),
    @SerialName("_id")
    @Contextual
    val id: Id<CompanyEntity> = newId(),
)

@Serializable
data class CompanyEntityData(
    val title: String,
    val codeName: String,
)