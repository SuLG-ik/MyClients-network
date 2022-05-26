package beauty.shafran.network.session.repository

import beauty.shafran.network.companies.data.CompanyStationId
import beauty.shafran.network.customers.data.CustomerId
import beauty.shafran.network.employees.data.EmployeeId
import beauty.shafran.network.session.data.DeactivateSessionRequestData
import beauty.shafran.network.session.data.ServiceSessionId
import beauty.shafran.network.session.entity.*
import beauty.shafran.network.utils.DatePeriod
import beauty.shafran.network.utils.PagedData
import beauty.shafran.network.utils.TransactionalScope

interface ServiceSessionsRepository {

    context (TransactionalScope) suspend fun throwIfSessionNotExists(sessionId: ServiceSessionId)

    context (TransactionalScope) suspend fun countUsagesForPeriod(
        period: DatePeriod,
        storageId: ServiceSessionStorageId,
        sessionId: ServiceSessionId? = null,
        employeeId: EmployeeId? = null,
    ): Int

    context (TransactionalScope) suspend fun countActivationsForPeriod(
        period: DatePeriod,
        storageId: ServiceSessionStorageId,
        sessionId: ServiceSessionId? = null,
        employeeId: EmployeeId? = null,
    ): Int

    context (TransactionalScope) suspend fun findUsagesForPeriod(
        period: DatePeriod,
        storageId: ServiceSessionStorageId,
    ): List<ServiceSessionUsageEntity>

    context (TransactionalScope) suspend fun countUsagesForSession(sessionId: ServiceSessionId): Long

    context (TransactionalScope) suspend fun findLastSessionForCustomer(
        customerId: CustomerId,
        storageId: ServiceSessionStorageId,
    ): ServiceSessionEntity

    context (TransactionalScope) suspend fun findSessionById(sessionId: ServiceSessionId): ServiceSessionEntity

    context (TransactionalScope) suspend fun findUsagesForSession(sessionId: ServiceSessionId): List<ServiceSessionUsageEntity>

    context (TransactionalScope) suspend fun findSessionsIgnoreDeactivatedForCustomer(
        customerId: CustomerId,
        storageId: ServiceSessionStorageId,
    ): List<ServiceSessionEntity>

    context (TransactionalScope) suspend fun isSessionExists(
        sessionId: ServiceSessionId,
    ): Boolean

    context (TransactionalScope) suspend fun useSession(
        data: ServiceSessionUsageDataEntity,
        sessionId: ServiceSessionId,
        stationId: CompanyStationId,
    ): ServiceSessionUsageEntity

    context (TransactionalScope) suspend fun findUsagesHistory(
        paged: PagedData,
        storageId: ServiceSessionStorageId,
    ): List<ServiceSessionUsageEntity>

    context (TransactionalScope) suspend fun insertSession(
        activation: ServiceSessionActivationEntity,
        storageId: ServiceSessionStorageId,
    ): ServiceSessionEntity

    context (TransactionalScope) suspend fun deactivateSessionForCustomer(
        sessionId: ServiceSessionId,
        data: DeactivateSessionRequestData,
    ): ServiceSessionEntity

    context (TransactionalScope) suspend fun findSessionsForCustomer(
        customerId: CustomerId,
        storageId: ServiceSessionStorageId,
    ): List<ServiceSessionEntity>
}