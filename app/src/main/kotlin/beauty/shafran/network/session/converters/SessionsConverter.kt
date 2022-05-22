package beauty.shafran.network.session.converters

import beauty.shafran.network.session.data.ServiceSession
import beauty.shafran.network.session.data.ServiceSessionUsage
import beauty.shafran.network.session.entity.ServiceSessionEntity
import beauty.shafran.network.session.entity.ServiceSessionUsageEntity
import beauty.shafran.network.utils.TransactionalScope

interface SessionsConverter {

    suspend fun TransactionalScope.toData(
        entity: ServiceSessionEntity,
        usageEntities: List<ServiceSessionUsageEntity>,
    ): ServiceSession

    suspend fun TransactionalScope.toData(entity: ServiceSessionUsageEntity): ServiceSessionUsage

}