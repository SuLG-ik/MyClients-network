package beauty.shafran.network.services.enity

import beauty.shafran.network.assets.entity.AssetEntity
import kotlinx.serialization.Contextual
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.litote.kmongo.Id
import org.litote.kmongo.newId

@Serializable
data class ServiceEntity(
    val info: ServiceInfoEntity,
    val image: AssetEntity? = null,
    @Contextual
    @SerialName("_id")
    val id: Id<ServiceEntity> = newId(),
    val configurations: List<ServiceConfigurationEntity> = emptyList(),
)

val ServiceEntity.Companion.collectionName get() = "services"

@Serializable
data class ServiceInfoEntity(
    val title: String,
    val description: String,
    val priority: Int = 1,
    val isPublic: Boolean = false,
)

@Serializable
data class ServiceConfigurationEntity(
    val title: String,
    val description: String,
    val cost: Int,
    val amount: Int,
    val deactivation: ServiceDeactivationEntity? = null,
    @Contextual
    @SerialName("_id")
    val id: Id<ServiceConfigurationEntity> = newId(),
)

@Serializable
data class ServiceDeactivationEntity(
    val reason: String,
    @Contextual
    @SerialName("_id")
    val id: Id<ServiceDeactivationEntity> = newId(),
)

