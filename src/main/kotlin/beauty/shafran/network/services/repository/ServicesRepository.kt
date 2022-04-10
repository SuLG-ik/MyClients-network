package beauty.shafran.network.services.repository

import beauty.shafran.network.services.enity.ServiceConfigurationEntity
import beauty.shafran.network.services.enity.ServiceEntity
import beauty.shafran.network.services.enity.ServiceInfoEntity

interface ServicesRepository {

    suspend fun updateServiceInfo(serviceId: String, info: ServiceInfoEntity): ServiceEntity

    suspend fun findServiceById(serviceId: String): ServiceEntity

    suspend fun findConfigurationForService(configurationId: String, serviceId: String): ServiceConfigurationEntity

    suspend fun findAllServices(offset: Int, page: Int): List<ServiceEntity>

    suspend fun createService(info: ServiceInfoEntity): ServiceEntity

    suspend fun addConfiguration(serviceId: String, configuration: ServiceConfigurationEntity): ServiceEntity

    suspend fun throwIfServiceNotExists(serviceId: String)
}