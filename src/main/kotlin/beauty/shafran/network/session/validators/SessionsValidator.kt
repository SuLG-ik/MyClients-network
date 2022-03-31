package beauty.shafran.network.session.validators

import beauty.shafran.network.session.data.CreateSessionForCustomerRequest
import beauty.shafran.network.session.data.GetSessionUsagesHistoryRequest
import beauty.shafran.network.session.data.GetSessionsForCustomerRequest
import beauty.shafran.network.session.data.UseSessionRequest

interface SessionsValidator {

    suspend fun getSessionUsagesHistory(data: GetSessionUsagesHistoryRequest): GetSessionUsagesHistoryRequest
    suspend fun getSessionsForCustomer(data: GetSessionsForCustomerRequest): GetSessionsForCustomerRequest
    suspend fun createSessionsForCustomer(data: CreateSessionForCustomerRequest): CreateSessionForCustomerRequest
    suspend fun useSession(data: UseSessionRequest): UseSessionRequest

}