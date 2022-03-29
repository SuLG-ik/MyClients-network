package beauty.shafran.network.session.repository

import beauty.shafran.network.session.data.*

interface ServiceSessionsRepository {

    suspend fun getLastSessionForCustomer(customerId: String): Session?
    suspend fun getSessionsForCustomer(data: GetSessionsForCustomerRequest): GetSessionsForCustomerResponse
    suspend fun createSessionsForCustomer(data: CreateSessionForCustomerRequest): CreateSessionForCustomerResponse
    suspend fun useSession(data: UseSessionRequest): UseSessionResponse

}