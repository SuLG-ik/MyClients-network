package beauty.shafran.network.session.executor

import beauty.shafran.network.assets.converter.AssetsConverter
import beauty.shafran.network.customers.converters.CustomersConverter
import beauty.shafran.network.customers.data.Customer
import beauty.shafran.network.customers.repository.CustomersRepository
import beauty.shafran.network.services.converter.ServicesConverter
import beauty.shafran.network.services.data.ConfiguredService
import beauty.shafran.network.services.repository.ServicesRepository
import beauty.shafran.network.session.SessionOveruseException
import beauty.shafran.network.session.converters.SessionsConverter
import beauty.shafran.network.session.data.*
import beauty.shafran.network.session.entity.SessionUsageDataEntity
import beauty.shafran.network.session.entity.SessionUsageEntity
import beauty.shafran.network.session.repository.SessionsRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class SessionsExecutorImpl(
) : SessionsExecutor, KoinComponent {

    private val sessionsConverter: SessionsConverter by inject()
    private val customersConverter: CustomersConverter by inject()
    private val assetsConverter: AssetsConverter by inject()

    private val servicesConverter: ServicesConverter by inject()
    private val sessionsRepository: SessionsRepository by inject()
    private val servicesRepository: ServicesRepository by inject()
    private val customersRepository: CustomersRepository by inject()


    override suspend fun getSessionUsagesHistory(data: GetSessionUsagesHistoryRequest): GetSessionUsagesHistoryResponse {
        val lastUsages = sessionsRepository.findUsagesHistory(
            offset = data.offset,
            page = data.page
        )
        return coroutineScope {
            GetSessionUsagesHistoryResponse(
                usages = lastUsages.map { loadSessionUsageHistoryItem(it) }
            )
        }
    }

    private suspend fun loadSessionUsageHistoryItem(entity: SessionUsageEntity): SessionUsageHistoryItem {
        return coroutineScope {
            val usage = async { with(sessionsConverter) { entity.toData() } }
            val session = async { sessionsRepository.findSessionById(entity.sessionId) }
            val configuration = async {
                servicesRepository.findConfigurationForService(
                    session.await().activation.configuration.configurationId,
                    session.await().activation.configuration.serviceId
                )
            }
            val service =
                async { servicesRepository.findServiceById(session.await().activation.configuration.serviceId) }
            val customer = async { customersRepository.findCustomerById(session.await().activation.customerId) }
            SessionUsageHistoryItem(
                service = ConfiguredService(
                    serviceId = service.await().id.toString(),
                    info = with(servicesConverter) { service.await().info.toData() },
                    image = with(assetsConverter) { service.await().image?.toData() },
                    configuration = with(servicesConverter) { configuration.await().toData() }
                ),
                usage = usage.await(),
                customer = with(customersConverter) { customer.await().toData() as Customer.ActivatedCustomer }
            )
        }

    }

    override suspend fun getSessionsForCustomer(data: GetSessionsForCustomerRequest): GetSessionsForCustomerResponse {
        return coroutineScope {
            val sessionEntities = sessionsRepository.findSessionsForCustomer(data.customerId)
            GetSessionsForCustomerResponse(
                sessions = sessionEntities.map {
                    with(sessionsConverter) { async { it.toData(sessionsRepository.findUsagesForSession(it.id.toString())) } }
                }.toList().awaitAll()
            )
        }
    }

    override suspend fun createSessionsForCustomer(data: CreateSessionForCustomerRequest): CreateSessionForCustomerResponse {
        return coroutineScope {
            val session = sessionsRepository.insertSession(with(sessionsConverter) { data.toNewEntity() })
            CreateSessionForCustomerResponse(
                session = with(sessionsConverter) { session.toData(sessionsRepository.findUsagesForSession(session.id.toString())) }
            )
        }
    }

    override suspend fun useSession(data: UseSessionRequest): UseSessionResponse {
        return coroutineScope {
            val session =
                sessionsRepository.findSessionById(data.sessionId)
            val configuration = async {
                servicesRepository.findConfigurationForService(
                    session.activation.configuration.configurationId,
                    session.activation.configuration.serviceId
                )
            }
            val usagesCount =
                async { sessionsRepository.countUsagesForSession(sessionId = session.id.toString()) }

            if (usagesCount.await() >= configuration.await().amount) {
                throw SessionOveruseException(data.sessionId)
            }
            sessionsRepository.useSession(
                SessionUsageEntity(
                    data = SessionUsageDataEntity(
                        employeeId = data.employeeId,
                        note = data.note,
                    ),
                    sessionId = session.id.toString(),
                )
            )
            UseSessionResponse(
                session = with(sessionsConverter) { session.toData(sessionsRepository.findUsagesForSession(session.id.toString())) }
            )
        }
    }


}