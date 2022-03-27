package beauty.shafran.network.session.repository

import beauty.shafran.network.services.data.GetServiceByIdRequest
import beauty.shafran.network.services.data.ServiceConfiguration
import beauty.shafran.network.services.repository.ServicesRepository
import beauty.shafran.network.session.converters.ServiceSessionsConverter
import beauty.shafran.network.session.data.*
import beauty.shafran.network.session.entity.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.withContext
import org.bson.types.ObjectId
import org.litote.kmongo.coroutine.CoroutineDatabase
import org.litote.kmongo.div
import org.litote.kmongo.eq
import org.litote.kmongo.id.toId
import org.litote.kmongo.toId

class MongoServiceSessionsRepository(
    client: CoroutineDatabase,
    private val sessionsConverter: ServiceSessionsConverter,
    private val servicesRepository: ServicesRepository,
) : ServiceSessionsRepository {

    private val collection = client.getCollection<SessionEntity>("sessions")

    override suspend fun getSessionsForCustomer(data: GetSessionsForCustomerRequest): GetSessionsForCustomerResponse {
        return coroutineScope {
            val sessionEntities =
                collection.find(SessionEntity::activation / SessionActivationEntity::customerId eq data.customerId)
                    .toFlow()

            GetSessionsForCustomerResponse(
                sessions = sessionEntities.map { with(sessionsConverter) { it.toData() } }.toList()
            )
        }
    }

    override suspend fun createSessionsForCustomer(data: CreateSessionForCustomerRequest): CreateSessionForCustomerResponse {
        return coroutineScope {
            val session = with(sessionsConverter) { data.toNewEntity() }
            collection.save(session)
            CreateSessionForCustomerResponse(
                session = with(sessionsConverter) { session.toData() }
            )
        }
    }

    override suspend fun useSession(data: UseSessionRequest): UseSessionResponse {
        val session =
            collection.findOneById(ObjectId(data.sessionId)) ?: TODO("SessionDoesNotExistsException(data.sessionId)")

        val configuration = findConfigurationForService(session.activation.configuration)
        if (session.usages.size >= configuration.amount)
            TODO("throw OveruseSessionException(configuration.id, configuration.amount)")

        val usageEntity = SessionUsageEntity(
            data = SessionUsageDataEntity(
                employeeId = data.employeeId,
                note = data.note,
            )
        )
        val newSession = session.copy(usages = session.usages + usageEntity)
            collection.save(newSession)
        return UseSessionResponse(
            session = with(sessionsConverter) { newSession.toData() }
        )
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

}