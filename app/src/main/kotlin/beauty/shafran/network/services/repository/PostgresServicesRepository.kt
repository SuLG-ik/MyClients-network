package beauty.shafran.network.services.repository

import beauty.shafran.ConfigurationNotExists
import beauty.shafran.ServiceNotExists
import beauty.shafran.network.services.data.ServiceConfigurationId
import beauty.shafran.network.services.data.ServiceId
import beauty.shafran.network.services.data.ServicesStorageId
import beauty.shafran.network.services.enity.*
import beauty.shafran.network.utils.PagedData
import beauty.shafran.network.utils.TransactionalScope
import beauty.shafran.network.utils.isRowExists
import beauty.shafran.network.utils.selectLatest
import kotlinx.coroutines.awaitAll
import org.jetbrains.exposed.sql.SortOrder
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.insertAndGetId
import org.jetbrains.exposed.sql.select


class PostgresServicesRepository : ServicesRepository {

    context (TransactionalScope)  override suspend fun updateServiceInfo(
        serviceId: ServiceId,
        info: ServiceInfoEntity,
    ): ServiceEntity {
        throwIfServiceNotExists(serviceId)
        ServicesDataTable.insert {
            it[this.serviceId] = serviceId.id
            it[this.title] = info.title
            it[this.description] = info.description
        }
        return findServiceById(serviceId)
    }

    context (TransactionalScope) private suspend fun findServiceInfo(serviceId: ServiceId): ServiceInfoEntity {
        val data = ServicesDataTable.selectLatest(serviceId.id) ?: throw ServiceNotExists(serviceId.toString())
        return ServiceInfoEntity(
            title = data[ServicesDataTable.title],
            description = data[ServicesDataTable.description],
        )
    }

    context (TransactionalScope) private suspend fun findServiceConfigurations(serviceId: ServiceId): List<ServiceConfigurationEntity> {
        val configurations =
            ServicesConfigurationsTable.select { ServicesConfigurationsTable.serviceId eq serviceId.id }
                .map { it[ServicesConfigurationsTable.id].value }
        val data = transactionAsync {
            ServicesConfigurationsDataTable.select { ServicesConfigurationsDataTable.configurationId inList configurations }
                .orderBy(ServicesConfigurationsDataTable.creationDate, SortOrder.ASC)
                .associateBy { it[ServicesConfigurationsDataTable.configurationId] }
        }
        val limits = transactionAsync {
            ServicesConfigurationsWithLimitTable.select { ServicesConfigurationsWithLimitTable.configurationId inList configurations }
                .orderBy(ServicesConfigurationsWithLimitTable.creationDate, SortOrder.ASC).associate {
                    it[ServicesConfigurationsWithLimitTable.configurationId].value to TypedServiceConfigurationEntity.WithAmountLimit(
                        amount = it[ServicesConfigurationsWithLimitTable.amount]
                    )
                }
        }
        return data.await().mapNotNull {
            ServiceConfigurationEntity(
                data = ServiceConfigurationEntityData(
                    title = it.value[ServicesConfigurationsDataTable.title],
                    description = it.value[ServicesConfigurationsDataTable.description],
                    cost = it.value[ServicesConfigurationsDataTable.cost],
                    parameters = limits.await()[it.key.value] ?: (return@mapNotNull null),
                ),
                id = ServiceConfigurationId(it.key.value),
                serviceId = serviceId
            )
        }
    }

    context (TransactionalScope) override suspend fun findServiceById(serviceId: ServiceId): ServiceEntity {
        throwIfServiceNotExists(serviceId)
        val info = transactionAsync { findServiceInfo(serviceId) }
        val configurations = transactionAsync { findServiceConfigurations(serviceId) }
        return ServiceEntity(
            info = info.await(),
            configurations = configurations.await(),
            id = serviceId,
        )

    }

    context (TransactionalScope) override suspend fun findServiceByConfigurationId(configurationId: ServiceConfigurationId): ServiceEntity {
        val configuration = findServiceConfiguration(configurationId)
        return findServiceById(configuration.serviceId)
    }

    context (TransactionalScope) override suspend fun findServiceConfiguration(configurationId: ServiceConfigurationId): ServiceConfigurationEntity {
        val serviceId =
            ServicesConfigurationsTable.selectLatest(configurationId.id)?.get(ServicesConfigurationsTable.serviceId)
                ?: throw ConfigurationNotExists(configurationId.toString())
        val dataDeferred = transactionAsync {
            ServicesConfigurationsDataTable.selectLatest(configurationId.id)!!
        }
        val limitDeferred =
            transactionAsync {
                ServicesConfigurationsWithLimitTable.selectLatest(configurationId.id)!![ServicesConfigurationsWithLimitTable.amount]
            }
        val data = dataDeferred.await()
        val limit = limitDeferred.await()
        return ServiceConfigurationEntity(
            data = ServiceConfigurationEntityData(
                title = data[ServicesConfigurationsDataTable.title],
                cost = data[ServicesConfigurationsDataTable.cost],
                description = data[ServicesConfigurationsDataTable.description],
                parameters = TypedServiceConfigurationEntity.WithAmountLimit(limit),
            ),
            id = configurationId,
            serviceId = ServiceId(serviceId.value),
        )
    }

    context (TransactionalScope) override suspend fun findAllServices(
        paged: PagedData,
        storageId: ServicesStorageId,
    ): List<ServiceEntity> {
        return ServicesToStorageTable.select { ServicesToStorageTable.storageId eq storageId.id }
            .map {
                transactionAsync { findServiceById(ServiceId(it[ServicesToStorageTable.serviceId].value)) }
            }.awaitAll()
    }

    context (TransactionalScope) override suspend fun createService(
        info: ServiceInfoEntity,
        storageId: ServicesStorageId,
    ): ServiceEntity {
        val serviceId = ServicesTable.insertAndGetId { }.value
        ServicesDataTable.insert {
            it[this.title] = info.title
            it[this.description] = info.description
        }
        return ServiceEntity(info = info, id = ServiceId(serviceId))
    }

    context (TransactionalScope) override suspend fun addConfiguration(
        serviceId: ServiceId,
        data: ServiceConfigurationEntityData,
    ): ServiceEntity {
        throwIfServiceNotExists(serviceId)
        val configurationId =
            ServicesConfigurationsTable.insertAndGetId { it[ServicesConfigurationsTable.serviceId] = serviceId.id }
        listOf(
            transactionAsync {
                ServicesConfigurationsDataTable.insert {
                    it[this.configurationId] = configurationId
                    it[this.cost] = data.cost
                    it[this.title] = data.title
                    it[this.description] = data.description
                }
            },
            transactionAsync {
                ServicesConfigurationsWithLimitTable.insert {
                    it[this.configurationId] = configurationId
                    it[this.amount] =
                        (data.parameters as TypedServiceConfigurationEntity.WithAmountLimit).amount
                }
            }
        ).awaitAll()
        return findServiceById(serviceId)
    }

    context (TransactionalScope) override suspend fun throwIfServiceNotExists(serviceId: ServiceId) {
        if (!isServiceExists(serviceId)) throw ServiceNotExists(serviceId.toString())
    }

    context (TransactionalScope) override suspend fun throwIfServiceConfigurationNotExists(configurationId: ServiceConfigurationId) {
        if (!isServiceConfigurationExists(configurationId)) throw ConfigurationNotExists(configurationId.toString())
    }

    context (TransactionalScope) override suspend fun isServiceConfigurationExists(configurationId: ServiceConfigurationId): Boolean {
        return ServicesConfigurationsTable.isRowExists(configurationId.id)
    }

    context (TransactionalScope) override suspend fun isServiceExists(serviceId: ServiceId): Boolean {
        return ServicesTable.isRowExists(serviceId.id)
    }
}