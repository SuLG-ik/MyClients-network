package beauty.shafran.network.services.repository

import beauty.shafran.network.ConfigurationNotExists
import beauty.shafran.network.ServiceNotExists
import beauty.shafran.network.services.enity.ServiceConfigurationEntity
import beauty.shafran.network.services.enity.ServiceEntity
import beauty.shafran.network.services.enity.ServiceInfoEntity
import beauty.shafran.network.services.enity.collectionName
import beauty.shafran.network.utils.paged
import beauty.shafran.network.utils.toIdSecure
import org.litote.kmongo.coroutine.CoroutineDatabase
import org.litote.kmongo.coroutine.updateOne
import org.litote.kmongo.div
import org.litote.kmongo.eq
import org.springframework.stereotype.Repository

@Repository
class MongoServicesRepository(coroutineDatabase: CoroutineDatabase) : ServicesRepository {

    private val servicesCollection = coroutineDatabase.getCollection<ServiceEntity>(ServiceEntity.collectionName)


    override suspend fun updateServiceInfo(serviceId: String, info: ServiceInfoEntity): ServiceEntity {
        val service = findServiceById(serviceId).copy(info = info)
        servicesCollection.updateOneById(serviceId.toIdSecure<ServiceEntity>("serviceId"), service)
        return service
    }

    override suspend fun findServiceById(serviceId: String): ServiceEntity {
        return servicesCollection.findOneById(serviceId.toIdSecure<ServiceEntity>("serviceId"))
            ?: throw ServiceNotExists(serviceId)
    }

    override suspend fun findConfigurationForService(
        configurationId: String,
        serviceId: String,
    ): ServiceConfigurationEntity {
        val service = findServiceById(serviceId)
        return service.configurations.firstOrNull { configurationId == it.id.toString() }
            ?: throw ConfigurationNotExists(configurationId)
    }

    override suspend fun findAllServices(offset: Int, page: Int): List<ServiceEntity> {
        return servicesCollection.find().descendingSort(ServiceEntity::info / ServiceInfoEntity::priority)
            .paged(offset, page)
            .toList()
    }


    override suspend fun createService(info: ServiceInfoEntity): ServiceEntity {
        val service = ServiceEntity(info = info)
        servicesCollection.insertOne(service)
        return service
    }

    override suspend fun addConfiguration(serviceId: String, configuration: ServiceConfigurationEntity): ServiceEntity {
        val service = findServiceById(serviceId).let { it.copy(configurations = it.configurations + configuration) }
        servicesCollection.updateOne(service)
        return service
    }


    override suspend fun throwIfServiceNotExists(serviceId: String) {
        if (servicesCollection.countDocuments(ServiceEntity::id eq serviceId.toIdSecure("serviceId")) < 1)
            throw ServiceNotExists(serviceId)
    }

}