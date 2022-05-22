package beauty.shafran.network.session.executor

import beauty.shafran.network.auth.data.AuthorizedAccount
import beauty.shafran.network.session.data.*

interface SessionsExecutor {

    suspend fun getSessionUsagesHistory(request: GetSessionUsagesHistoryRequest, account: AuthorizedAccount): GetSessionUsagesHistoryResponse
    suspend fun createSessionsForCustomer(request: CreateSessionForCustomerRequest, account: AuthorizedAccount): CreateSessionForCustomerResponse
    suspend fun useSession(request: UseSessionRequest, account: AuthorizedAccount): UseSessionResponse
    suspend fun deactivateSession(request: DeactivateSessionRequest, account: AuthorizedAccount): DeactivateSessionResponse
    suspend fun getAllSessionsForCustomer(request: GetSessionsForCustomerRequest, account: AuthorizedAccount): GetSessionsForCustomerResponse
    suspend fun getSessionsIgnoreDeactivatedForCustomer(request: GetSessionsForCustomerRequest, account: AuthorizedAccount): GetSessionsForCustomerResponse
    suspend fun getSessionsStats(request: GetSessionsStatsRequest, account: AuthorizedAccount): GetSessionsStatsResponse

}