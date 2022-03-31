package beauty.shafran.network.session.executor

import beauty.shafran.network.session.data.*

interface SessionsExecutor {

    suspend fun getSessionUsagesHistory(data: GetSessionUsagesHistoryRequest): GetSessionUsagesHistoryResponse
    suspend fun getSessionsForCustomer(data: GetSessionsForCustomerRequest): GetSessionsForCustomerResponse
    suspend fun createSessionsForCustomer(data: CreateSessionForCustomerRequest): CreateSessionForCustomerResponse
    suspend fun useSession(data: UseSessionRequest): UseSessionResponse

}