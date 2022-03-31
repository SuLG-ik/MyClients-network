package beauty.shafran.network.session.repository

import beauty.shafran.network.customers.exceptions.CustomerNotExists
import beauty.shafran.network.customers.repository.CustomersRepository
import beauty.shafran.network.session.SessionNotExists
import beauty.shafran.network.session.entity.*
import beauty.shafran.network.utils.paged
import beauty.shafran.network.utils.toIdSecure
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.litote.kmongo.`in`
import org.litote.kmongo.coroutine.CoroutineDatabase
import org.litote.kmongo.div
import org.litote.kmongo.eq

class MongoSessionsRepository(coroutineDatabase: CoroutineDatabase) : SessionsRepository, KoinComponent {

    private val sessionsCollection = coroutineDatabase.getCollection<SessionEntity>(SessionEntity.collectionName)
    private val usagesCollection =
        coroutineDatabase.getCollection<SessionUsageEntity>(SessionUsageEntity.collectionName)
    private val customerRepository: CustomersRepository by inject()

    override suspend fun isSessionExists(sessionId: String): Boolean {
        return sessionsCollection.countDocuments(SessionEntity::id eq sessionId.toIdSecure("sessionId")) >= 1
    }

    override suspend fun useSession(data: SessionUsageEntity): SessionUsageEntity {
        usagesCollection.insertOne(data)
        return data
    }

    override suspend fun findUsagesHistory(offset: Int, page: Int): List<SessionUsageEntity> {
        return usagesCollection.find()
            .descendingSort(SessionUsageEntity::data / SessionUsageDataEntity::date)
            .paged(offset, page)
            .toList()
    }

    override suspend fun throwIfSessionNotExists(sessionId: String) {
        if (!isSessionExists(sessionId))
            throw SessionNotExists(sessionId)
    }

    override suspend fun countUsagesForSession(sessionId: String): Long {
        if (!isSessionExists(sessionId))
            throw SessionNotExists(sessionId)
        return usagesCollection.countDocuments(SessionUsageEntity::sessionId eq sessionId)
    }

    override suspend fun findSessionById(sessionId: String): SessionEntity {
        return sessionsCollection.findOneById(sessionId.toIdSecure<SessionEntity>("sessionId"))
            ?: throw SessionNotExists(sessionId)
    }

    override suspend fun findUsagesForSession(sessionId: String): List<SessionUsageEntity> {
        if (!isSessionExists(sessionId))
            throw SessionNotExists(sessionId)
        return usagesCollection.find(SessionUsageEntity::sessionId eq sessionId).toList()
    }

    override suspend fun findSessionsForCustomer(customerId: String): List<SessionEntity> {
        if (!customerRepository.isCustomerExists(customerId))
            throw CustomerNotExists(customerId)
        return sessionsCollection.find(SessionEntity::activation / SessionActivationEntity::customerId eq customerId)
            .toList()
    }

    override suspend fun insertSession(activation: SessionActivationEntity): SessionEntity {
        return SessionEntity(activation = activation).also { sessionsCollection.insertOne(it) }
    }

    override suspend fun findLastUsageForCustomer(sessions: List<SessionEntity>): SessionUsageEntity? {
        return usagesCollection.find(SessionUsageEntity::sessionId `in` sessions.map { it.id.toString() })
            .descendingSort(SessionUsageEntity::data / SessionUsageDataEntity::date)
            .limit(1)
            .first()
    }

}