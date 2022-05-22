package beauty.shafran.network.services.repository

import beauty.shafran.network.services.data.ServiceConfigurationId
import beauty.shafran.network.services.data.ServiceId
import beauty.shafran.network.services.data.ServicesStorageId
import beauty.shafran.network.services.enity.ServiceConfigurationEntity
import beauty.shafran.network.services.enity.ServiceConfigurationEntityData
import beauty.shafran.network.services.enity.ServiceEntity
import beauty.shafran.network.services.enity.ServiceInfoEntity
import beauty.shafran.network.utils.PagedData
import beauty.shafran.network.utils.TransactionalScope

interface ServicesRepository {

            suspend fun TransactionalScope.updateServiceInfo(serviceId: ServiceId, info: ServiceInfoEntity): ServiceEntity

            suspend fun TransactionalScope.findServiceById(serviceId: ServiceId): ServiceEntity

            suspend fun TransactionalScope.findServiceByConfigurationId(configurationId: ServiceConfigurationId): ServiceEntity

            suspend fun TransactionalScope.findServiceConfiguration(configurationId: ServiceConfigurationId): ServiceConfigurationEntity

            suspend fun TransactionalScope.findAllServices(paged: PagedData, storageId: ServicesStorageId): List<ServiceEntity>

            suspend fun TransactionalScope.createService(info: ServiceInfoEntity, storageId: ServicesStorageId): ServiceEntity

            suspend fun TransactionalScope.addConfiguration(serviceId: ServiceId, data: ServiceConfigurationEntityData): ServiceEntity

            suspend fun TransactionalScope.throwIfServiceNotExists(serviceId: ServiceId)

            suspend fun TransactionalScope.isServiceExists(serviceId: ServiceId): Boolean

            suspend fun TransactionalScope.throwIfServiceConfigurationNotExists(configurationId: ServiceConfigurationId)

            suspend fun TransactionalScope.isServiceConfigurationExists(configurationId: ServiceConfigurationId): Boolean

}