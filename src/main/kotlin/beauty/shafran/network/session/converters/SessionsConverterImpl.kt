package beauty.shafran.network.session.converters

import beauty.shafran.network.employees.data.Employee
import beauty.shafran.network.employees.data.GetEmployeeByIdRequest
import beauty.shafran.network.employees.executor.EmployeesExecutor
import beauty.shafran.network.services.data.ConfiguredService
import beauty.shafran.network.services.data.GetServiceByIdRequest
import beauty.shafran.network.services.executor.ServicesExecutor
import beauty.shafran.network.session.data.*
import beauty.shafran.network.session.entity.*
import beauty.shafran.network.session.executor.SessionsExecutor
import beauty.shafran.network.utils.getZonedDateTime
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.time.ZoneId
import java.time.ZonedDateTime

class SessionsConverterImpl : SessionsConverter, KoinComponent {


    private val employeesExecutor: EmployeesExecutor by inject()
    private val servicesExecutor: ServicesExecutor by inject()
    private val sessionsRepository: SessionsExecutor by inject()

    override suspend fun SessionUsageEntity.toData(): SessionUsage {
        return SessionUsage(
            id = id.toString(),
            data = SessionUsageData(
                employee = findEmployee(data.employeeId),
                note = data.note,
                date = ZonedDateTime.ofInstant(data.date.toInstant(), ZoneId.systemDefault())
            )
        )
    }

    override suspend fun SessionEntity.toData(usageEntities: List<SessionUsageEntity>): Session {
        return coroutineScope {
            val activation = async { findActivation(activation) }
            val usages = async { findUsages(usageEntities) }
            val deactivation = async { findDeactivation(deactivation) }
            Session(
                id = id.toString(),
                activation = activation.await(),
                usages = usages.await(),
                manualDeactivation = deactivation.await(),
            )
        }
    }

    override suspend fun CreateSessionForCustomerRequest.toNewEntity(): SessionActivationEntity {
        return SessionActivationEntity(
            configuration = SessionConfigurationEntity(
                configurationId = configuration.configurationId,
                serviceId = configuration.serviceId,
            ),
            customerId = customerId,
            employeeId = employeeId,
            note = data.note
        )
    }

    private suspend fun findActivation(activationEntity: SessionActivationEntity): SessionActivation {
        return coroutineScope {
            val activationEmployee = async(Dispatchers.IO) { findEmployee(activationEntity.employeeId) }
            val configuredService = async(Dispatchers.IO) { findConfiguredService(activationEntity.configuration) }
            SessionActivation(
                employee = activationEmployee.await(),
                service = configuredService.await(),
                customerId = activationEntity.customerId.toString(),
                note = activationEntity.note,
            )
        }
    }

    private suspend fun findDeactivation(deactivationEntity: SessionManualDeactivationEntity?): SessionManualDeactivation? {
        if (deactivationEntity == null) return null
        return SessionManualDeactivation(id = deactivationEntity.id.toString(),
            data = SessionManualDeactivationData(
                reason = deactivationEntity.data.reason,
                employee = findEmployee(deactivationEntity.data.employeeId),
                note = deactivationEntity.data.note,
                date = deactivationEntity.id.getZonedDateTime(),
            ))
    }

    private suspend fun findEmployee(employeeId: String): Employee {
        return employeesExecutor.getEmployeeById(GetEmployeeByIdRequest(employeeId)).employee
    }

    private suspend fun findConfiguredService(configurationEntity: SessionConfigurationEntity): ConfiguredService {
        val serviceRequest =
            servicesExecutor.getServiceById(GetServiceByIdRequest(configurationEntity.serviceId.toString()))
        val service =
            serviceRequest.service
                ?: TODO("throw ServiceDoesNotExistsException(configurationEntity.serviceId.toHexString())")

        val configuration =
            service.data.configurations.firstOrNull { it.id == configurationEntity.configurationId.toString() }
                ?: TODO("""throw ConfigurationDoesNotExistsException(
                    serviceId = configurationEntity.serviceId.toHexString(),
                    configurationId = configurationEntity.configurationId.toHexString(),
                )""")

        return ConfiguredService(
            serviceId = service.id,
            info = service.data.info,
            image = service.data.image,
            configuration = configuration,
        )
    }

    private suspend fun findUsages(usages: List<SessionUsageEntity>): List<SessionUsage> {
        return coroutineScope {
            usages.associateWith {
                async(Dispatchers.IO) { employeesExecutor.getEmployeeById(GetEmployeeByIdRequest(it.data.employeeId.toString())) }
            }.map {
                val employee = it.value.await().employee
                SessionUsage(id = it.key.id.toString(),
                    data = SessionUsageData(employee = employee,
                        note = it.key.data.note,
                        date = it.key.id.getZonedDateTime()))
            }
        }
    }


}