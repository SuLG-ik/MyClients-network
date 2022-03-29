package beauty.shafran.network.session.repository

import beauty.shafran.network.services.data.GetServiceByIdRequest
import beauty.shafran.network.services.data.ServiceConfiguration
import beauty.shafran.network.services.repository.ServicesRepository
import beauty.shafran.network.session.converters.ServiceSessionsConverter
import beauty.shafran.network.session.data.*
import beauty.shafran.network.session.entity.*
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import org.bson.types.ObjectId
import org.litote.kmongo.`in`
import org.litote.kmongo.coroutine.CoroutineDatabase
import org.litote.kmongo.div
import org.litote.kmongo.eq
import org.litote.kmongo.id.WrappedObjectId

class MongoServiceSessionsRepository(
    client: CoroutineDatabase,
    private val sessionsConverter: ServiceSessionsConverter,
    private val servicesRepository: ServicesRepository,
) : ServiceSessionsRepository {

    private val sessions = client.getCollection<SessionEntity>("sessions")
    private val usages = client.getCollection<SessionUsageEntity>("usages")




    override suspend fun getLastSessionForCustomer(customerId: String): Session? {
        val customerSessions = findSessionsForCustomer(customerId)
        val sessionUsages = usages.find(SessionUsageEntity::sessionId `in` customerSessions.map { it.id.toString() })
            .descendingSort(SessionUsageEntity::data / SessionUsageDataEntity::date)
            .limit(1)
        val sessionUsage = sessionUsages.first() ?: return null
        return with(sessionsConverter) {
            customerSessions.find { it.id.toString() == sessionUsage.sessionId }
                ?.run { toData(findUsagesForSession(id.toString())) }
        }
    }

    override suspend fun getSessionsForCustomer(data: GetSessionsForCustomerRequest): GetSessionsForCustomerResponse {
        return coroutineScope {
            val sessionEntities = findSessionsForCustomer(data.customerId)


            GetSessionsForCustomerResponse(
                sessions = sessionEntities.map {
                    with(sessionsConverter) { async { it.toData(findUsagesForSession(it.id.toString())) } }
                }.toList().awaitAll()
            )
        }
    }

    override suspend fun createSessionsForCustomer(data: CreateSessionForCustomerRequest): CreateSessionForCustomerResponse {
        return coroutineScope {
            val session = with(sessionsConverter) { data.toNewEntity() }
            sessions.save(session)
            CreateSessionForCustomerResponse(
                session = with(sessionsConverter) { session.toData(findUsagesForSession(session.id.toString())) }
            )
        }
    }

    override suspend fun useSession(data: UseSessionRequest): UseSessionResponse {
        return coroutineScope {
            val session =
                sessions.findOneById(ObjectId(data.sessionId)) ?: TODO("SessionDoesNotExistsException(data.sessionId)")

            val configuration = async { findConfigurationForService(session.activation.configuration) }
            val usagesCount = async { countUsagesForSession(sessionId = session.id.toString()) }

            if (usagesCount.await() >= configuration.await().amount) {
                TODO("throw ServiceDoesNotExistsException(configuration.serviceId.toHexString())")
            }
            val usageEntity = SessionUsageEntity(
                data = SessionUsageDataEntity(
                    employeeId = data.employeeId,
                    note = data.note,
                ),
                sessionId = session.id.toString(),
            )
            usages.save(usageEntity)
            UseSessionResponse(
                session = with(sessionsConverter) { session.toData(findUsagesForSession(session.id.toString())) }
            )
        }
    }


    private suspend fun countUsagesForSession(sessionId: String): Long {
        if (sessions.countDocuments(SessionEntity::id eq WrappedObjectId(sessionId)) < 1)
            TODO("throw ServiceDoesNotExistsException(configuration.serviceId.toHexString())")
        return usages.countDocuments(SessionUsageEntity::sessionId eq sessionId)
    }


    private suspend fun findUsagesForSession(sessionId: String): List<SessionUsageEntity> {
        return usages.find(SessionUsageEntity::sessionId eq sessionId).toList()
    }

    private suspend fun findConfigurationForService(configuration: SessionConfigurationEntity): ServiceConfiguration {
        val service = servicesRepository.getServiceById(GetServiceByIdRequest(configuration.serviceId.toString()))
        if (service.service == null) {
            TODO("throw ServiceDoesNotExistsException(configuration.serviceId.toHexString())")
        }
        return service.service.data.configurations.firstOrNull {
            it.id == configuration.configurationId.toString()
        } ?: TODO("""throw ConfigurationDoesNotExistsException(
            configuration.serviceId.toHexString(),
            configuration.configurationId.toHexString()
        )""")
    }

    private suspend fun findSessionsForCustomer(customerId: String): List<SessionEntity> {
        return sessions.find(SessionEntity::activation / SessionActivationEntity::customerId eq customerId)
            .toList()
    }

}