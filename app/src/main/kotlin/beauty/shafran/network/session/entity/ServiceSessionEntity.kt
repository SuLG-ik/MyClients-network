package beauty.shafran.network.session.entity

import beauty.shafran.network.companies.data.CompanyStationId
import beauty.shafran.network.companies.entity.CompanyStationTable
import beauty.shafran.network.customers.data.CustomerId
import beauty.shafran.network.customers.entity.CustomerTable
import beauty.shafran.network.employees.data.EmployeeId
import beauty.shafran.network.employees.entity.EmployeeTable
import beauty.shafran.network.services.data.ServiceConfigurationId
import beauty.shafran.network.services.enity.ServicesConfigurationsTable
import beauty.shafran.network.session.data.*
import beauty.shafran.network.utils.LongIdWithMetaTable
import beauty.shafran.network.utils.MetaEntity
import beauty.shafran.network.utils.customEnumeration
import ru.sulgik.exposed.TableToCreation

@TableToCreation
object ServiceSessionTable : LongIdWithMetaTable("service_session") {
    val configurationId = reference("configuration", ServicesConfigurationsTable)
    val customerId = reference("customer", CustomerTable)
    val employeeId = reference("employee", EmployeeTable)
    val stationId = reference("station", CompanyStationTable)
}

@TableToCreation
object ServiceSessionToStorageTable : LongIdWithMetaTable("service_session_to_storage") {
    val serviceSessionId = reference("service_session", ServiceSessionTable)
    val storageId = reference("storage", ServiceSessionsStorageTable)
}

@TableToCreation
object ServiceSessionDataTable : LongIdWithMetaTable("service_session_data") {
    val serviceSessionId = reference("service_session", ServiceSessionTable)
    val note = text("note").nullable().default(null)
}

@TableToCreation
object ServiceSessionUsageTable : LongIdWithMetaTable("service_session_usage") {
    val serviceSessionId = reference("service_session", ServiceSessionTable)
    val stationId = reference("station", CompanyStationTable)
}

@TableToCreation
object ServiceSessionEmployeeToUsageTable : LongIdWithMetaTable("service_session_employee_to_usage") {
    val usageId = reference("usage", ServiceSessionUsageTable)
    val employeeId = reference("employee", EmployeeTable)
}

@TableToCreation
object ServiceSessionUsageDataTable : LongIdWithMetaTable("service_service_usage_data") {
    val usageId = reference("usage", ServiceSessionUsageTable)
    val note = text("note").nullable().default(null)
}

@TableToCreation
object ServiceSessionDeactivationTable : LongIdWithMetaTable("service_session_deactivation") {
    val serviceSessionId = reference("service_session", ServiceSessionTable)
    val employeeId = reference("employee", EmployeeTable)
}

@TableToCreation
object ServiceSessionDeactivationDataTable : LongIdWithMetaTable("service_session_deactivation_data") {
    val reason =
        customEnumeration<SessionManualDeactivationReason>("reason", SessionManualDeactivationReason.typeName)
    val note = text("note").nullable().default(null)
    val deactivationId = reference("deactivation", ServiceSessionDeactivationTable)
}

data class ServiceSessionEntity(
    val activation: ServiceSessionActivationEntity,
    val deactivation: ServiceSessionManualDeactivationEntity?,
    val meta: MetaEntity,
    val id: ServiceSessionId,
)

data class ServiceSessionActivationEntity(
    val configuration: ServiceConfigurationId,
    val employeeId: EmployeeId,
    val customerId: CustomerId,
    val stationId: CompanyStationId,
    val note: String,
)

data class ServiceSessionUsageEntity(
    val sessionId: ServiceSessionId,
    val stationId: CompanyStationId,
    val data: ServiceSessionUsageDataEntity,
    val id: ServiceSessionUsageId,
)


data class ServiceSessionUsageDataEntity(
    val employeeId: EmployeeId,
    val meta: MetaEntity,
    val note: String,
)

data class ServiceSessionManualDeactivationEntity(
    val data: ServiceSessionManualDeactivationDataEntity,
    val meta: MetaEntity,
    val id: ServiceSessionDeactivationId,
)


data class ServiceSessionManualDeactivationDataEntity(
    val reason: SessionManualDeactivationReason,
    val employeeId: EmployeeId,
    val note: String,
)

