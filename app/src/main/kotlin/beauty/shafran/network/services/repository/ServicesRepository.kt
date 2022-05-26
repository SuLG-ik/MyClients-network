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

    context (TransactionalScope) suspend fun updateServiceInfo(serviceId: ServiceId, info: ServiceInfoEntity): ServiceEntity

    context (TransactionalScope) suspend fun findServiceById(serviceId: ServiceId): ServiceEntity

    context (TransactionalScope) suspend fun findServiceByConfigurationId(configurationId: ServiceConfigurationId): ServiceEntity

    context (TransactionalScope) suspend fun findServiceConfiguration(configurationId: ServiceConfigurationId): ServiceConfigurationEntity

    context (TransactionalScope) suspend fun findAllServices(paged: PagedData, storageId: ServicesStorageId): List<ServiceEntity>

    context (TransactionalScope) suspend fun createService(info: ServiceInfoEntity, storageId: ServicesStorageId): ServiceEntity

    context (TransactionalScope) suspend fun addConfiguration(serviceId: ServiceId, data: ServiceConfigurationEntityData): ServiceEntity

    context (TransactionalScope) suspend fun throwIfServiceNotExists(serviceId: ServiceId)

    context (TransactionalScope) suspend fun isServiceExists(serviceId: ServiceId): Boolean

    context (TransactionalScope) suspend fun throwIfServiceConfigurationNotExists(configurationId: ServiceConfigurationId)

    context (TransactionalScope) suspend fun isServiceConfigurationExists(configurationId: ServiceConfigurationId): Boolean

}