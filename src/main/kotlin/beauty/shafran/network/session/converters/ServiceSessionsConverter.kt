package beauty.shafran.network.session.converters

import beauty.shafran.network.session.data.CreateSessionForCustomerRequest
import beauty.shafran.network.session.data.Session
import beauty.shafran.network.session.entity.SessionEntity
import beauty.shafran.network.session.entity.SessionUsageEntity

interface ServiceSessionsConverter {

    suspend fun CreateSessionForCustomerRequest.toNewEntity(): SessionEntity

    suspend fun SessionEntity.toData(usageEnitties: List<SessionUsageEntity>): Session
}