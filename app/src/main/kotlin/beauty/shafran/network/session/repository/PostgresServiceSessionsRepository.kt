package beauty.shafran.network.session.repository

import beauty.shafran.SessionNotExists
import beauty.shafran.SessionOveruseException
import beauty.shafran.network.companies.data.CompanyStationId
import beauty.shafran.network.customers.data.CustomerId
import beauty.shafran.network.employees.data.EmployeeId
import beauty.shafran.network.services.data.ServiceConfigurationId
import beauty.shafran.network.services.enity.TypedServiceConfigurationEntity
import beauty.shafran.network.services.repository.ServicesRepository
import beauty.shafran.network.session.data.*
import beauty.shafran.network.session.entity.*
import beauty.shafran.network.utils.*
import kotlinx.coroutines.awaitAll
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.jetbrains.exposed.sql.alias
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.insertAndGetId
import org.jetbrains.exposed.sql.select
import org.koin.core.annotation.Single

@Single
class PostgresServiceSessionsRepository(
    private val servicesRepository: ServicesRepository,
) : ServiceSessionsRepository {

    override suspend fun TransactionalScope.throwIfSessionNotExists(sessionId: ServiceSessionId) {
        if (!isSessionExists(sessionId)) throw SessionNotExists(sessionId.toString())
    }

    override suspend fun TransactionalScope.isSessionExists(sessionId: ServiceSessionId): Boolean {
        return ServiceSessionTable.isRowExists(sessionId.id)
    }

    override suspend fun TransactionalScope.countUsagesForPeriod(
        period: DatePeriod,
        storageId: ServiceSessionStorageId,
        sessionId: ServiceSessionId?,
        employeeId: EmployeeId?,
    ): Int {
        TODO("Not yet implemented")
    }

    override suspend fun TransactionalScope.countActivationsForPeriod(
        period: DatePeriod,
        storageId: ServiceSessionStorageId,
        sessionId: ServiceSessionId?,
        employeeId: EmployeeId?,
    ): Int {
        TODO("Not yet implemented")
    }

    override suspend fun TransactionalScope.findUsagesForPeriod(
        period: DatePeriod,
        storageId: ServiceSessionStorageId,
    ): List<ServiceSessionUsageEntity> {
        TODO("Not yet implemented")
    }

    override suspend fun TransactionalScope.countUsagesForSession(sessionId: ServiceSessionId): Long {
        return ServiceSessionUsageTable.alias("pisos")
            .select { ServiceSessionUsageTable.serviceSessionId eq sessionId.id }
            .count()
    }

    override suspend fun TransactionalScope.findLastSessionForCustomer(
        customerId: CustomerId,
        storageId: ServiceSessionStorageId,
    ): ServiceSessionEntity {
        TODO("Not yet implemented")
    }

    private suspend fun TransactionalScope.findSessionDeactivation(sessionId: ServiceSessionId): ServiceSessionManualDeactivationEntity? {
        return ServiceSessionDeactivationTable.selectLatest { ServiceSessionDeactivationTable.serviceSessionId eq sessionId.id }
            ?.let { deactivation ->
                val id = deactivation[ServiceSessionDeactivationTable.id].value
                val data = ServiceSessionDeactivationDataTable.selectLatest {
                    ServiceSessionDeactivationDataTable.deactivationId eq id
                }
                ServiceSessionManualDeactivationEntity(
                    data = ServiceSessionManualDeactivationDataEntity(reason = data?.get(
                        ServiceSessionDeactivationDataTable.reason) ?: SessionManualDeactivationReason.UNKNOWN,
                        employeeId = EmployeeId(deactivation[ServiceSessionDeactivationTable.employeeId].value),
                        note = data?.get(ServiceSessionDeactivationDataTable.note).orEmpty()),
                    id = ServiceSessionDeactivationId(id),
                    meta = MetaEntity(deactivation[ServiceSessionDeactivationTable.creationDate])
                )
            }
    }

    override suspend fun TransactionalScope.findSessionById(sessionId: ServiceSessionId): ServiceSessionEntity {
        val sessionDeferred = transactionAsync {
            ServiceSessionTable.selectLatest { ServiceSessionTable.id eq sessionId.id } ?: throw SessionNotExists(
                sessionId.toString())
        }
        val dataDeferred = transactionAsync {
            ServiceSessionDataTable.selectLatest { ServiceSessionDataTable.serviceSessionId eq sessionId.id }
        }
        val deactivationDeferred = transactionAsync {
            findSessionDeactivation(sessionId)
        }
        val session = sessionDeferred.await()
        val data = dataDeferred.await()
        return ServiceSessionEntity(activation = ServiceSessionActivationEntity(
            configuration = ServiceConfigurationId(
                session[ServiceSessionTable.configurationId].value),
            employeeId = EmployeeId(session[ServiceSessionTable.employeeId].value),
            customerId = CustomerId(session[ServiceSessionTable.customerId].value),
            note = data?.get(ServiceSessionDataTable.note).orEmpty(),
            stationId = CompanyStationId(session[ServiceSessionTable.stationId].value),
        ),
            deactivation = deactivationDeferred.await(),
            meta = MetaEntity(session[ServiceSessionTable.creationDate]),
            id = sessionId)
    }

    override suspend fun TransactionalScope.findUsagesForSession(sessionId: ServiceSessionId): List<ServiceSessionUsageEntity> {
        val usages =
            ServiceSessionUsageTable.select { ServiceSessionUsageTable.serviceSessionId eq sessionId.id }
                .associateBy { it[ServiceSessionUsageTable.id].value }
        val usagesDataDeferred = transactionAsync {
            ServiceSessionUsageDataTable
                .select { ServiceSessionUsageDataTable.usageId inList usages.keys }
                .associateBy { it[ServiceSessionUsageDataTable.usageId].value }
        }
        val usagesEmployeeDeferred = transactionAsync {
            ServiceSessionEmployeeToUsageTable.select {
                ServiceSessionEmployeeToUsageTable.usageId inList usages.keys
            }.groupBy { it[ServiceSessionEmployeeToUsageTable.usageId].value }
        }
        val usagesData = usagesDataDeferred.await()
        val usagesEmployee = usagesEmployeeDeferred.await()
        return usages.mapNotNull {
            val data = usagesData[it.key]
            val employee = usagesEmployee[it.key]?.firstOrNull()?.get(ServiceSessionEmployeeToUsageTable.employeeId)
                ?: return@mapNotNull null
            ServiceSessionUsageEntity(
                sessionId = sessionId,
                data = ServiceSessionUsageDataEntity(
                    note = data?.getOrNull(ServiceSessionUsageDataTable.note).orEmpty(),
                    employeeId = EmployeeId(employee.value),
                    meta = MetaEntity(it.value[ServiceSessionUsageTable.creationDate]),
                ),
                stationId = CompanyStationId(it.value[ServiceSessionUsageTable.stationId].value),
                id = ServiceSessionUsageId(it.key),
            )
        }
    }

    override suspend fun TransactionalScope.findSessionsIgnoreDeactivatedForCustomer(
        customerId: CustomerId,
        storageId: ServiceSessionStorageId,
    ): List<ServiceSessionEntity> {
        return findSessionsForCustomer(customerId, storageId).filter { it.deactivation != null }
    }

    override suspend fun TransactionalScope.useSession(
        data: ServiceSessionUsageDataEntity,
        sessionId: ServiceSessionId,
        stationId: CompanyStationId,
    ): ServiceSessionUsageEntity {
        val session =
            ServiceSessionTable.selectLatest(sessionId.id) ?: throw SessionNotExists(sessionId.toString())
        val configuration =
            with(servicesRepository) { findServiceConfiguration(ServiceConfigurationId(session[ServiceSessionTable.configurationId].value)) }
        return when (val parameters = configuration.data.parameters) {
            is TypedServiceConfigurationEntity.WithAmountLimit -> {
                val currentUsages = countUsagesForSession(sessionId)
                if (currentUsages >= parameters.amount)
                    throw SessionOveruseException(sessionId.toString())
                val usageId = ServiceSessionUsageTable.insertAndGetId {
                    it[this.stationId] = stationId.id
                    it[this.serviceSessionId] = sessionId.id
                }
                val employeeDeferred = transactionAsync {
                    ServiceSessionEmployeeToUsageTable.insert {
                        it[this.employeeId] = data.employeeId.id
                        it[this.usageId] = usageId
                    }
                }
                if (data.note.isNotEmpty())
                    ServiceSessionUsageDataTable.insert {
                        it[this.usageId] = usageId
                        it[this.note] = data.note
                    }
                employeeDeferred.await()
                ServiceSessionUsageEntity(
                    data = data,
                    stationId = stationId,
                    sessionId = sessionId,
                    id = ServiceSessionUsageId(usageId.value),
                )
            }
        }
    }

    override suspend fun TransactionalScope.findUsagesHistory(
        paged: PagedData,
        storageId: ServiceSessionStorageId,
    ): List<ServiceSessionUsageEntity> {
        TODO("Not yet implemented")
    }

    override suspend fun TransactionalScope.insertSession(
        activation: ServiceSessionActivationEntity,
        storageId: ServiceSessionStorageId,
    ): ServiceSessionEntity {

        val creationDate = Clock.System.now().toLocalDateTime(TimeZone.UTC)
        val id = ServiceSessionTable.insertAndGetId {
            it[this.configurationId] = activation.configuration.id
            it[this.employeeId] = activation.employeeId.id
            it[this.customerId] = activation.customerId.id
            it[this.stationId] = activation.stationId.id
            it[this.creationDate] = creationDate
        }.value
        val trimmedNote = activation.note.trim()
        if (trimmedNote.isNotEmpty()) ServiceSessionDataTable.insert {
            it[this.serviceSessionId] = id
            it[this.note] = trimmedNote
        }
        return ServiceSessionEntity(
            activation = activation,
            deactivation = null,
            meta = MetaEntity(creationDate),
            id = ServiceSessionId(id),
        )
    }

    override suspend fun TransactionalScope.deactivateSessionForCustomer(
        sessionId: ServiceSessionId,
        data: DeactivateSessionRequestData,
    ): ServiceSessionEntity {
        throwIfSessionNotExists(sessionId)
        val deactivationId = ServiceSessionDeactivationTable.insertAndGetId {
            it[this.serviceSessionId] = sessionId.id
            it[this.employeeId] = data.employeeId.id
        }
        ServiceSessionDeactivationDataTable.insert {
            it[this.deactivationId] = deactivationId
            it[this.note] = note
            it[this.reason] = data.reason
        }
        return findSessionById(sessionId)
    }

    override suspend fun TransactionalScope.findSessionsForCustomer(
        customerId: CustomerId,
        storageId: ServiceSessionStorageId,
    ): List<ServiceSessionEntity> {
        val serviceSessions =
            ServiceSessionToStorageTable.select { ServiceSessionToStorageTable.storageId eq storageId.id }
                .map { it[ServiceSessionToStorageTable.serviceSessionId] }
        return ServiceSessionTable.select { ServiceSessionTable.id inList serviceSessions }
            .map { transactionAsync { findSessionById(ServiceSessionId(it[ServiceSessionTable.id].value)) } }
            .awaitAll()
    }

}