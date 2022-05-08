package beauty.shafran.network.session.converters

import beauty.shafran.ConfigurationNotExists
import beauty.shafran.network.employees.converters.EmployeesConverter
import beauty.shafran.network.employees.data.Employee
import beauty.shafran.network.employees.repository.EmployeesRepository
import beauty.shafran.network.services.converter.ServicesConverter
import beauty.shafran.network.services.data.ConfiguredService
import beauty.shafran.network.services.repository.ServicesRepository
import beauty.shafran.network.session.data.*
import beauty.shafran.network.session.entity.*
import beauty.shafran.network.utils.getZonedDateTime
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import org.koin.core.annotation.Single
import java.time.ZoneId
import java.time.ZonedDateTime

@Single
class SessionsConverterImpl(
    private val servicesRepository: ServicesRepository,
    private val servicesConverter: ServicesConverter,
    private val employeesRepository: EmployeesRepository,
    private val employeesConverter: EmployeesConverter,
) : SessionsConverter {

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
            note = data.remark
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
                date = ZonedDateTime.ofInstant(activationEntity.date.toInstant(), ZoneId.systemDefault())
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
        return with(employeesConverter) { employeesRepository.findEmployeeById(employeeId).toData() }
    }

    private suspend fun findConfiguredService(configurationEntity: SessionConfigurationEntity): ConfiguredService {
        val service = with(servicesConverter) {
            servicesRepository.findServiceById(configurationEntity.serviceId).toData()
        }
        val configuration =
            service.data.configurations.firstOrNull { it.id == configurationEntity.configurationId }
                ?: throw ConfigurationNotExists(configurationEntity.configurationId)

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
                async(Dispatchers.IO) { employeesRepository.findEmployeeById(it.data.employeeId) }
            }.map {
                val employee = it.value.await()
                SessionUsage(id = it.key.id.toString(),
                    data = SessionUsageData(employee = with(employeesConverter) { employee.toData() },
                        note = it.key.data.note,
                        date = it.key.id.getZonedDateTime()))
            }
        }
    }


}