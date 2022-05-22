package beauty.shafran.network.auth.repository

import beauty.shafran.network.account.data.AccountId
import beauty.shafran.network.auth.entity.AccountSessionEntity
import beauty.shafran.network.auth.entity.AccountSessionId
import beauty.shafran.network.utils.TransactionalScope

interface AccountSessionsRepository {

    suspend fun TransactionalScope.createSessionForAccount(accountId: AccountId): AccountSessionEntity

    suspend fun TransactionalScope.findSessionWithId(sessionId: AccountSessionId): AccountSessionEntity

}