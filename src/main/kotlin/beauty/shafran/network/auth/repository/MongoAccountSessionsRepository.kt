package beauty.shafran.network.auth.repository

import beauty.shafran.network.AccountSessionNotExists
import beauty.shafran.network.auth.entity.AccountSessionEntity
import beauty.shafran.network.utils.toIdSecure
import org.litote.kmongo.coroutine.CoroutineCollection
import org.springframework.stereotype.Repository

@Repository
class MongoAccountSessionsRepository(
    private val accountSessionsCollection: CoroutineCollection<AccountSessionEntity>,
) : AccountSessionsRepository {

    override suspend fun createSessionForAccount(accountId: String): AccountSessionEntity {
        val session = AccountSessionEntity(accountId = accountId)
        accountSessionsCollection.insertOne(session)
        return session
    }

    override suspend fun findSessionWithId(sessionId: String): AccountSessionEntity {
        return accountSessionsCollection.findOneById(sessionId.toIdSecure<AccountSessionEntity>("sessionId"))
            ?: throw AccountSessionNotExists(sessionId)
    }

}