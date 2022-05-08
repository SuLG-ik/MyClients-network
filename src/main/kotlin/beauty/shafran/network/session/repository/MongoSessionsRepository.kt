package beauty.shafran.network.session.repository

import beauty.shafran.CustomerNotExists
import beauty.shafran.SessionIsAlreadyUsed
import beauty.shafran.SessionNotExists
import beauty.shafran.network.companies.entity.CompanyReferenceEntity
import beauty.shafran.network.customers.repository.CustomersRepository
import beauty.shafran.network.session.data.DeactivateSessionRequestData
import beauty.shafran.network.session.entity.*
import beauty.shafran.network.utils.DatePeriod
import beauty.shafran.network.utils.paged
import beauty.shafran.network.utils.toIdSecure
import beauty.shafran.network.utils.toStartOfDate
import org.koin.core.annotation.Single
import org.litote.kmongo.*
import org.litote.kmongo.coroutine.CoroutineDatabase

@Single
class MongoSessionsRepository(
    coroutineDatabase: CoroutineDatabase,
    private val customerRepository: CustomersRepository,
) : SessionsRepository {

    private val sessionsCollection = coroutineDatabase.getCollection<SessionEntity>(SessionEntity.collectionName)
    private val usagesCollection =
        coroutineDatabase.getCollection<SessionUsageEntity>(SessionUsageEntity.collectionName)


    override suspend fun countUsagesForPeriod(
        period: DatePeriod,
        companyId: String,
        sessionId: String?,
        employeeId: String?,
    ): Int {
        return usagesCollection.countDocuments(
            and(
                SessionUsageEntity::data / SessionUsageDataEntity::date gte period.from.toStartOfDate(),
                SessionUsageEntity::data / SessionUsageDataEntity::date lt period.to.plusDays(1).toStartOfDate(),
                SessionUsageEntity::companyReference eq CompanyReferenceEntity(companyId)
            )
        ).toInt()
    }

    override suspend fun countActivationsForPeriod(
        period: DatePeriod,
        companyId: String,
        sessionId: String?,
        employeeId: String?,
    ): Int {
        return sessionsCollection.countDocuments(
            and(
                SessionEntity::activation / SessionActivationEntity::date gte period.from.toStartOfDate(),
                SessionEntity::activation / SessionActivationEntity::date lt period.to.plusDays(1).toStartOfDate(),
                SessionEntity::deactivation eq null,
                SessionEntity::companyReference eq CompanyReferenceEntity(companyId)
            )
        ).toInt()
    }

    override suspend fun findUsagesForPeriod(
        period: DatePeriod,
        companyId: String,
    ): List<SessionUsageEntity> {
        return usagesCollection.find(
            and(
                SessionUsageEntity::data / SessionUsageDataEntity::date gte period.from.toStartOfDate(),
                SessionUsageEntity::data / SessionUsageDataEntity::date lt period.to.plusDays(1).toStartOfDate(),
                SessionEntity::companyReference eq CompanyReferenceEntity(companyId)
            )
        ).toList()
    }

    override suspend fun isSessionExists(sessionId: String): Boolean {
        return sessionsCollection.countDocuments(SessionEntity::id eq sessionId.toIdSecure("sessionId")) >= 1
    }

    override suspend fun useSession(data: SessionUsageEntity): SessionUsageEntity {
        usagesCollection.insertOne(data)
        return data
    }

    override suspend fun findUsagesHistory(offset: Int, page: Int, companyId: String): List<SessionUsageEntity> {
        return usagesCollection.find(SessionUsageEntity::companyReference eq CompanyReferenceEntity(companyId))
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
        return sessionsCollection.findOneById(sessionId.toIdSecure("sessionId"))
            ?: throw SessionNotExists(sessionId)
    }

    override suspend fun findUsagesForSession(sessionId: String): List<SessionUsageEntity> {
        if (!isSessionExists(sessionId))
            throw SessionNotExists(sessionId)
        return usagesCollection.find(SessionUsageEntity::sessionId eq sessionId).toList()
    }

    override suspend fun findSessionsIgnoreDeactivatedForCustomer(customerId: String): List<SessionEntity> {
        if (!customerRepository.isCustomerExists(customerId))
            throw CustomerNotExists(customerId)
        return sessionsCollection.find(
            and(
                SessionEntity::activation / SessionActivationEntity::customerId eq customerId,
                SessionEntity::deactivation eq null,
            )
        ).toList()
    }

    override suspend fun findSessionsForCustomer(customerId: String): List<SessionEntity> {
        if (!customerRepository.isCustomerExists(customerId))
            throw CustomerNotExists(customerId)
        return sessionsCollection.find(
            and(
                SessionEntity::activation / SessionActivationEntity::customerId eq customerId,
            )
        ).toList()
    }

    override suspend fun deactivateSessionForCustomer(
        sessionId: String,
        data: DeactivateSessionRequestData,
    ): SessionEntity {
        val usagesCount = countUsagesForSession(sessionId)
        if (usagesCount > 0)
            throw SessionIsAlreadyUsed(sessionId)
        val deactivation = SessionManualDeactivationEntity(
            data = SessionManualDeactivationDataEntity(
                employeeId = data.employeeId,
                note = data.note,
                reason = data.reason
            )
        )
        val newSession = findSessionById(sessionId).copy(deactivation = deactivation)
        return updateSession(newSession)
    }

    private suspend fun updateSession(session: SessionEntity): SessionEntity {
        sessionsCollection.save(session)
        return session
    }

    override suspend fun insertSession(activation: SessionActivationEntity, companyId: String): SessionEntity {
        return SessionEntity(
            activation = activation,
            companyReference = CompanyReferenceEntity(companyId),
        ).also { sessionsCollection.insertOne(it) }
    }

    override suspend fun findLastUsageForCustomer(sessions: List<SessionEntity>): SessionUsageEntity? {
        return usagesCollection.find(SessionUsageEntity::sessionId `in` sessions.map { it.id.toString() })
            .descendingSort(SessionUsageEntity::data / SessionUsageDataEntity::date)
            .limit(1)
            .first()
    }

}