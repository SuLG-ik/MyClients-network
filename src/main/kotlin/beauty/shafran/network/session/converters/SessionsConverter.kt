package beauty.shafran.network.session.converters

import beauty.shafran.network.session.data.CreateSessionForCustomerRequest
import beauty.shafran.network.session.data.Session
import beauty.shafran.network.session.data.SessionUsage
import beauty.shafran.network.session.entity.SessionActivationEntity
import beauty.shafran.network.session.entity.SessionEntity
import beauty.shafran.network.session.entity.SessionUsageEntity

interface SessionsConverter {

    suspend fun CreateSessionForCustomerRequest.toNewEntity(): SessionActivationEntity

    suspend fun SessionEntity.toData(usageEntities: List<SessionUsageEntity>): Session

    suspend fun SessionUsageEntity.toData(): SessionUsage

}