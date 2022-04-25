package beauty.shafran.network.auth.repository

import beauty.shafran.network.auth.entity.AccountSessionEntity

interface AccountSessionsRepository {

    suspend fun createSessionForAccount(accountId: String): AccountSessionEntity

    suspend fun findSessionWithId(sessionId: String): AccountSessionEntity

}