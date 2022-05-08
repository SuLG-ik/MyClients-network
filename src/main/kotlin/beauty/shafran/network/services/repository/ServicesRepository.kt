package beauty.shafran.network.services.repository

import beauty.shafran.network.services.enity.ServiceConfigurationEntity
import beauty.shafran.network.services.enity.ServiceEntity
import beauty.shafran.network.services.enity.ServiceInfoEntity

interface ServicesRepository {

    suspend fun updateServiceInfo(serviceId: String, info: ServiceInfoEntity): ServiceEntity

    suspend fun findServiceById(serviceId: String): ServiceEntity

    suspend fun findConfigurationForService(configurationId: String, serviceId: String): ServiceConfigurationEntity

    suspend fun findAllServices(offset: Int, page: Int, companyId: String): List<ServiceEntity>

    suspend fun createService(info: ServiceInfoEntity, companyId: String): ServiceEntity

    suspend fun addConfiguration(serviceId: String, configuration: ServiceConfigurationEntity): ServiceEntity

    suspend fun throwIfServiceNotExists(serviceId: String)

}