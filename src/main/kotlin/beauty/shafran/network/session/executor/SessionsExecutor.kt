package beauty.shafran.network.session.executor

import beauty.shafran.network.session.data.*

interface SessionsExecutor {

    suspend fun getSessionUsagesHistory(request: GetSessionUsagesHistoryRequest): GetSessionUsagesHistoryResponse
    suspend fun createSessionsForCustomer(request: CreateSessionForCustomerRequest): CreateSessionForCustomerResponse
    suspend fun useSession(request: UseSessionRequest): UseSessionResponse
    suspend fun deactivateSession(request: DeactivateSessionRequest): DeactivateSessionResponse
    suspend fun getAllSessionsForCustomer(request: GetSessionsForCustomerRequest): GetSessionsForCustomerResponse
    suspend fun getSessionsIgnoreDeactivatedForCustomer(request: GetSessionsForCustomerRequest): GetSessionsForCustomerResponse
    suspend fun getSessionsStats(request: GetSessionsStatsRequest): GetSessionsStatsResponse

}