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

    suspend fun TransactionalScope.throwIfSessionNotExists(sessionId: ServiceSessionId)

    suspend fun TransactionalScope.countUsagesForPeriod(
        period: DatePeriod,
        storageId: ServiceSessionStorageId,
        sessionId: ServiceSessionId? = null,
        employeeId: EmployeeId? = null,
    ): Int

    suspend fun TransactionalScope.countActivationsForPeriod(
        period: DatePeriod,
        storageId: ServiceSessionStorageId,
        sessionId: ServiceSessionId? = null,
        employeeId: EmployeeId? = null,
    ): Int

    suspend fun TransactionalScope.findUsagesForPeriod(
        period: DatePeriod,
        storageId: ServiceSessionStorageId,
    ): List<ServiceSessionUsageEntity>

    suspend fun TransactionalScope.countUsagesForSession(sessionId: ServiceSessionId): Long

    suspend fun TransactionalScope.findLastSessionForCustomer(
        customerId: CustomerId,
        storageId: ServiceSessionStorageId,
    ): ServiceSessionEntity

    suspend fun TransactionalScope.findSessionById(sessionId: ServiceSessionId): ServiceSessionEntity

    suspend fun TransactionalScope.findUsagesForSession(sessionId: ServiceSessionId): List<ServiceSessionUsageEntity>

    suspend fun TransactionalScope.findSessionsIgnoreDeactivatedForCustomer(
        customerId: CustomerId,
        storageId: ServiceSessionStorageId,
    ): List<ServiceSessionEntity>

    suspend fun TransactionalScope.isSessionExists(
        sessionId: ServiceSessionId,
    ): Boolean

    suspend fun TransactionalScope.useSession(
        data: ServiceSessionUsageDataEntity,
        sessionId: ServiceSessionId,
        stationId: CompanyStationId,
    ): ServiceSessionUsageEntity

    suspend fun TransactionalScope.findUsagesHistory(
        paged: PagedData,
        storageId: ServiceSessionStorageId,
    ): List<ServiceSessionUsageEntity>

    suspend fun TransactionalScope.insertSession(
        activation: ServiceSessionActivationEntity,
        storageId: ServiceSessionStorageId,
    ): ServiceSessionEntity

    suspend fun TransactionalScope.deactivateSessionForCustomer(
        sessionId: ServiceSessionId,
        data: DeactivateSessionRequestData,
    ): ServiceSessionEntity

    suspend fun TransactionalScope.findSessionsForCustomer(
        customerId: CustomerId,
        storageId: ServiceSessionStorageId,
    ): List<ServiceSessionEntity>
}