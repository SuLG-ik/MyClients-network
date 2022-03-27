package beauty.shafran.network.session.entity

import beauty.shafran.network.customers.entity.CustomerEntity
import beauty.shafran.network.employees.data.Employee
import beauty.shafran.network.employees.entity.EmployeeEntity
import beauty.shafran.network.services.enity.ServiceConfigurationEntity
import beauty.shafran.network.services.enity.ServiceEntity
import beauty.shafran.network.session.data.SessionManualDeactivationReason
import kotlinx.serialization.Contextual
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.litote.kmongo.Id
import org.litote.kmongo.newId

@Serializable
data class SessionEntity(
    val activation: SessionActivationEntity,
    val deactivation: SessionManualDeactivationEntity? = null,
    val usages: List<SessionUsageEntity> = emptyList(),
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
    val data: SessionUsageDataEntity,
    @SerialName("_id")
    @Contextual
    val id: Id<SessionUsageEntity> = newId(),
)

@Serializable
data class SessionUsageDataEntity(
    val employeeId: String,
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

