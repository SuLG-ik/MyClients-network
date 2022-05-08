package beauty.shafran.network.auth.repository

import beauty.shafran.AccountSessionNotExists
import beauty.shafran.network.auth.entity.AccountSessionEntity
import beauty.shafran.network.utils.toIdSecure
import org.koin.core.annotation.Named
import org.koin.core.annotation.Single
import org.litote.kmongo.coroutine.CoroutineCollection

@Single
class MongoAccountSessionsRepository(
    @Named(AccountSessionEntity.collectionName)
    private val accountSessionsCollection: CoroutineCollection<AccountSessionEntity>,
) : AccountSessionsRepository {

    override suspend fun createSessionForAccount(accountId: String): AccountSessionEntity {
        val session = AccountSessionEntity(accountId = accountId)
        accountSessionsCollection.insertOne(session)
        return session
    }

    override suspend fun findSessionWithId(sessionId: String): AccountSessionEntity {
        return accountSessionsCollection.findOneById(sessionId.toIdSecure("sessionId"))
            ?: throw AccountSessionNotExists(sessionId)
    }

}