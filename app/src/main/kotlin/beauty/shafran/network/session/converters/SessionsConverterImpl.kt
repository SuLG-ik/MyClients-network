package beauty.shafran.network.session.converters

import beauty.shafran.network.employees.converters.EmployeesConverter
import beauty.shafran.network.employees.data.Employee
import beauty.shafran.network.employees.data.EmployeeId
import beauty.shafran.network.employees.repository.EmployeesRepository
import beauty.shafran.network.services.converter.ServicesConverter
import beauty.shafran.network.services.data.ConfiguredService
import beauty.shafran.network.services.data.ServiceConfigurationId
import beauty.shafran.network.services.repository.ServicesRepository
import beauty.shafran.network.session.data.*
import beauty.shafran.network.session.entity.ServiceSessionActivationEntity
import beauty.shafran.network.session.entity.ServiceSessionEntity
import beauty.shafran.network.session.entity.ServiceSessionManualDeactivationEntity
import beauty.shafran.network.session.entity.ServiceSessionUsageEntity
import beauty.shafran.network.utils.TransactionalScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope


class SessionsConverterImpl(
    private val servicesRepository: ServicesRepository,
    private val servicesConverter: ServicesConverter,
    private val employeesRepository: EmployeesRepository,
    private val employeesConverter: EmployeesConverter,
) : SessionsConverter {

    override suspend fun TransactionalScope.toData(entity: ServiceSessionUsageEntity): ServiceSessionUsage {
        return ServiceSessionUsage(
            id = entity.id.toString(),
            data = ServiceSessionUsageData(
                employee = findEmployee(entity.data.employeeId),
                note = entity.data.note,
                date = entity.data.meta.creationDate
            )
        )
    }

    override suspend fun TransactionalScope.toData(
        entity: ServiceSessionEntity,
        usageEntities: List<ServiceSessionUsageEntity>,
    ): ServiceSession {
        val activation = transactionAsync { findActivation(entity.activation) }
        val usages = transactionAsync { findUsages(usageEntities) }
        val deactivation = transactionAsync { findDeactivation(entity.deactivation) }
        return ServiceSession(
            id = entity.id,
            activation = activation.await(),
            usages = usages.await(),
            manualDeactivation = deactivation.await(),
            date = entity.meta.creationDate,
        )
    }

    private suspend fun TransactionalScope.findActivation(activationEntity: ServiceSessionActivationEntity): ServiceSessionActivation {
        return coroutineScope {
            val activationEmployee = async(Dispatchers.IO) { findEmployee(activationEntity.employeeId) }
            val configuredService = async(Dispatchers.IO) { findConfiguredService(activationEntity.configuration) }
            ServiceSessionActivation(
                employee = activationEmployee.await(),
                service = configuredService.await(),
                customerId = activationEntity.customerId.toString(),
                note = activationEntity.note,
            )
        }
    }

    private suspend fun TransactionalScope.findDeactivation(deactivationEntity: ServiceSessionManualDeactivationEntity?): ServiceSessionManualDeactivation? {
        if (deactivationEntity == null) return null
        return ServiceSessionManualDeactivation(
            id = deactivationEntity.id.toString(),
            data = SessionManualDeactivationData(
                reason = deactivationEntity.data.reason,
                employee = findEmployee(deactivationEntity.data.employeeId),
                note = deactivationEntity.data.note,
                date = deactivationEntity.meta.creationDate
            ),
        )
    }

    private suspend fun TransactionalScope.findEmployee(employeeId: EmployeeId): Employee {
        val entity = transactionAsync { employeesRepository.run { findEmployeeById(employeeId) } }
        val data = transactionAsync { employeesRepository.run { findEmployeeDataById(employeeId) } }
        val layoff = transactionAsync { employeesRepository.run { findEmployeeLayoffById(employeeId) } }
        val image = transactionAsync { employeesRepository.run { findEmployeeImageById(employeeId) } }
        return employeesConverter.buildEmployee(
            employeeEntity = entity.await(),
            employeeDataEntity = data.await(),
            employeeLayoffEntity = layoff.await(),
            image = image.await()
        )
    }

    private suspend fun TransactionalScope.findConfiguredService(configurationId: ServiceConfigurationId): ConfiguredService {
        val configuration = with(servicesConverter) {
            with(servicesRepository) { findServiceConfiguration(configurationId) }.toData()
        }

        val service = with(servicesConverter) {
            with(servicesRepository) { findServiceById(configuration.serviceId) }.toData()
        }

        return ConfiguredService(
            serviceId = service.id,
            info = service.data.info,
            image = service.data.image,
            configuration = configuration,
        )
    }

    private suspend fun TransactionalScope.findUsages(usages: List<ServiceSessionUsageEntity>): List<ServiceSessionUsage> {
        return coroutineScope {
            usages.associateWith {
                async(Dispatchers.IO) {
                    with(employeesRepository) { findEmployeeById(it.data.employeeId) }
                }
            }.map {
                val employee = it.value.await()
                ServiceSessionUsage(
                    id = it.key.id.toString(),
                    data = ServiceSessionUsageData(
                        employee = findEmployee(EmployeeId(employee.id)),
                        note = it.key.data.note,
                        date = it.key.data.meta.creationDate
                    )
                )
            }
        }
    }


}