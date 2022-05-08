package beauty.shafran.network.services.repository

import beauty.shafran.ConfigurationNotExists
import beauty.shafran.ServiceNotExists
import beauty.shafran.network.companies.entity.CompanyReferenceEntity
import beauty.shafran.network.services.enity.ServiceConfigurationEntity
import beauty.shafran.network.services.enity.ServiceEntity
import beauty.shafran.network.services.enity.ServiceInfoEntity
import beauty.shafran.network.services.enity.collectionName
import beauty.shafran.network.utils.paged
import beauty.shafran.network.utils.toIdSecure
import org.koin.core.annotation.Single
import org.litote.kmongo.coroutine.CoroutineDatabase
import org.litote.kmongo.div
import org.litote.kmongo.eq

@Single
class MongoServicesRepository(coroutineDatabase: CoroutineDatabase) : ServicesRepository {

    private val servicesCollection = coroutineDatabase.getCollection<ServiceEntity>(ServiceEntity.collectionName)


    override suspend fun updateServiceInfo(serviceId: String, info: ServiceInfoEntity): ServiceEntity {
        val service = findServiceById(serviceId).copy(info = info)
        servicesCollection.save(service)
        return service
    }

    override suspend fun findServiceById(serviceId: String): ServiceEntity {
        return servicesCollection.findOneById(serviceId.toIdSecure("serviceId"))
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

    override suspend fun findAllServices(offset: Int, page: Int, companyId: String): List<ServiceEntity> {
        return servicesCollection.find(
            ServiceEntity::companyReference eq CompanyReferenceEntity(companyId)
        ).descendingSort(ServiceEntity::info / ServiceInfoEntity::priority)
            .paged(offset, page)
            .toList()
    }


    override suspend fun createService(info: ServiceInfoEntity, companyId: String): ServiceEntity {
        val service = ServiceEntity(info = info, companyReference = CompanyReferenceEntity(companyId))
        servicesCollection.insertOne(service)
        return service
    }

    override suspend fun addConfiguration(serviceId: String, configuration: ServiceConfigurationEntity): ServiceEntity {
        val service =
            servicesCollection.findOneById(serviceId.toIdSecure("serviceId")) ?: throw ServiceNotExists(serviceId)
        val newService = service.copy(configurations = service.configurations + configuration)
        servicesCollection.save(newService)
        return newService
    }


    override suspend fun throwIfServiceNotExists(serviceId: String) {
        if (servicesCollection.countDocuments(ServiceEntity::id eq serviceId.toIdSecure("serviceId")) < 1)
            throw ServiceNotExists(serviceId)
    }

}