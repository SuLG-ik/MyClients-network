package beauty.shafran.network.session.entity

import beauty.shafran.network.session.data.SessionManualDeactivationReason
import kotlinx.serialization.Contextual
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.litote.kmongo.Id
import org.litote.kmongo.newId
import java.util.*

@Serializable
data class SessionEntity(
    val activation: SessionActivationEntity,
    val deactivation: SessionManualDeactivationEntity? = null,
    @Contextual
    @SerialName("_id")
    val id: Id<SessionEntity> = newId(),
)

@Serializable
data class SessionConfigurationEntity(
    val serviceId: String,
    val configurationId: String,
)

@Serializable
data class SessionActivationEntity(
    val configuration: SessionConfigurationEntity,
    val employeeId: String,
    val customerId: String,
    val note: String? = null,
)

@Serializable
data class SessionUsageEntity(
    val sessionId: String,
    val data: SessionUsageDataEntity,
    @SerialName("_id")
    @Contextual
    val id: Id<SessionUsageEntity> = newId(),
)

@Serializable
data class SessionUsageDataEntity(
    val employeeId: String,
    @Contextual
    val date: Date = Date(),
    val note: String? = null,
)

@Serializable
data class SessionManualDeactivationEntity(
    val data: SessionManualDeactivationDataEntity,
    @SerialName("_id")
    @Contextual
    val id: Id<SessionManualDeactivationEntity> = newId(),
)

@Serializable
data class SessionManualDeactivationDataEntity(
    val reason: SessionManualDeactivationReason,
    val employeeId: String,
    val note: String? = null,
)

