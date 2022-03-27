package beauty.shafran.network.session.converters

import beauty.shafran.network.session.data.CreateSessionForCustomerRequest
import beauty.shafran.network.session.data.Session
import beauty.shafran.network.session.entity.SessionEntity

interface ServiceSessionsConverter {

    suspend fun SessionEntity.toData(): Session

    suspend fun CreateSessionForCustomerRequest.toNewEntity(): SessionEntity

}