package beauty.shafran.network.session.repository

import beauty.shafran.network.session.data.DeactivateSessionRequestData
import beauty.shafran.network.session.entity.SessionActivationEntity
import beauty.shafran.network.session.entity.SessionEntity
import beauty.shafran.network.session.entity.SessionUsageEntity
import beauty.shafran.network.utils.DatePeriod

interface SessionsRepository {

    suspend fun throwIfSessionNotExists(sessionId: String)

    suspend fun countUsagesForPeriod(
        period: DatePeriod,
        companyId: String,
        sessionId: String? = null,
        employeeId: String? = null,
    ): Int

    suspend fun countActivationsForPeriod(
        period: DatePeriod,
        companyId: String,
        sessionId: String? = null,
        employeeId: String? = null,
    ): Int

    suspend fun findUsagesForPeriod(
        period: DatePeriod,
        companyId: String,
    ): List<SessionUsageEntity>


    suspend fun countUsagesForSession(sessionId: String): Long
    suspend fun findSessionById(sessionId: String): SessionEntity
    suspend fun findUsagesForSession(sessionId: String): List<SessionUsageEntity>
    suspend fun findSessionsIgnoreDeactivatedForCustomer(customerId: String): List<SessionEntity>
    suspend fun findLastUsageForCustomer(sessions: List<SessionEntity>): SessionUsageEntity?
    suspend fun isSessionExists(
        sessionId: String,
    ): Boolean

    suspend fun useSession(data: SessionUsageEntity): SessionUsageEntity
    suspend fun findUsagesHistory(offset: Int, page: Int, companyId: String): List<SessionUsageEntity>
    suspend fun insertSession(activation: SessionActivationEntity, companyId: String): SessionEntity
    suspend fun deactivateSessionForCustomer(sessionId: String, data: DeactivateSessionRequestData): SessionEntity
    suspend fun findSessionsForCustomer(customerId: String): List<SessionEntity>
}