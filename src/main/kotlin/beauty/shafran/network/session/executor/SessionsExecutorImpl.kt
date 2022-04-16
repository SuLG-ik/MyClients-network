package beauty.shafran.network.session.executor

import beauty.shafran.network.SessionIsDeactivated
import beauty.shafran.network.SessionOveruseException
import beauty.shafran.network.assets.converter.AssetsConverter
import beauty.shafran.network.customers.converters.CustomersConverter
import beauty.shafran.network.customers.data.Customer
import beauty.shafran.network.customers.repository.CustomersRepository
import beauty.shafran.network.services.converter.ServicesConverter
import beauty.shafran.network.services.data.ConfiguredService
import beauty.shafran.network.services.repository.ServicesRepository
import beauty.shafran.network.session.converters.SessionsConverter
import beauty.shafran.network.session.data.*
import beauty.shafran.network.session.entity.SessionUsageDataEntity
import beauty.shafran.network.session.entity.SessionUsageEntity
import beauty.shafran.network.session.repository.SessionsRepository
import beauty.shafran.network.utils.toLocalDate
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import org.springframework.stereotype.Service

@Service
class SessionsExecutorImpl(
    private val sessionsConverter: SessionsConverter,
    private val customersConverter: CustomersConverter,
    private val assetsConverter: AssetsConverter,
    private val servicesConverter: ServicesConverter,
    private val sessionsRepository: SessionsRepository,
    private val servicesRepository: ServicesRepository,
    private val customersRepository: CustomersRepository,
) : SessionsExecutor {


    override suspend fun getSessionUsagesHistory(request: GetSessionUsagesHistoryRequest): GetSessionUsagesHistoryResponse {
        val lastUsages = sessionsRepository.findUsagesHistory(
            offset = request.offset,
            page = request.page
        )
        return coroutineScope {
            GetSessionUsagesHistoryResponse(
                usages = lastUsages.mapNotNull { loadSessionUsageHistoryItem(it) },
                offset = request.offset,
                page = request.page,
            )
        }
    }

    private suspend fun loadSessionUsageHistoryItem(entity: SessionUsageEntity): SessionUsageHistoryItem? {
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
            val customer = async { customersRepository.findCustomerByIdOrNull(session.await().activation.customerId) }
            SessionUsageHistoryItem(
                service = ConfiguredService(
                    serviceId = service.await().id.toString(),
                    info = with(servicesConverter) { service.await().info.toData() },
                    image = with(assetsConverter) { service.await().image?.toData() },
                    configuration = with(servicesConverter) { configuration.await().toData() }
                ),
                usage = usage.await(),
                customer = with(customersConverter) { customer.await()?.toData() as? Customer.ActivatedCustomer }
                    ?: return@coroutineScope null
            )
        }

    }

    override suspend fun getAllSessionsForCustomer(request: GetSessionsForCustomerRequest): GetSessionsForCustomerResponse {
        return coroutineScope {
            val sessionEntities = sessionsRepository.findSessionsForCustomer(request.customerId)
            GetSessionsForCustomerResponse(
                sessions = sessionEntities.map {
                    with(sessionsConverter) { async { it.toData(sessionsRepository.findUsagesForSession(it.id.toString())) } }
                }.toList().awaitAll()
            )
        }
    }

    override suspend fun getSessionsIgnoreDeactivatedForCustomer(request: GetSessionsForCustomerRequest): GetSessionsForCustomerResponse {
        return coroutineScope {
            val sessionEntities = sessionsRepository.findSessionsIgnoreDeactivatedForCustomer(request.customerId)
            GetSessionsForCustomerResponse(
                sessions = sessionEntities.map {
                    with(sessionsConverter) { async { it.toData(sessionsRepository.findUsagesForSession(it.id.toString())) } }
                }.toList().awaitAll()
            )
        }
    }

    override suspend fun createSessionsForCustomer(request: CreateSessionForCustomerRequest): CreateSessionForCustomerResponse {
        return coroutineScope {
            val session = sessionsRepository.insertSession(with(sessionsConverter) { request.toNewEntity() })
            CreateSessionForCustomerResponse(
                session = with(sessionsConverter) { session.toData(sessionsRepository.findUsagesForSession(session.id.toString())) }
            )
        }
    }

    override suspend fun useSession(request: UseSessionRequest): UseSessionResponse {
        return coroutineScope {
            val session =
                sessionsRepository.findSessionById(request.sessionId)
            if (session.deactivation != null)
                throw SessionIsDeactivated(request.sessionId)
            val configuration = async {
                servicesRepository.findConfigurationForService(
                    session.activation.configuration.configurationId,
                    session.activation.configuration.serviceId
                )
            }
            val usagesCount =
                async { sessionsRepository.countUsagesForSession(sessionId = request.sessionId.toString()) }

            if (usagesCount.await() >= configuration.await().amount) {
                throw SessionOveruseException(request.sessionId)
            }
            sessionsRepository.useSession(
                SessionUsageEntity(
                    data = SessionUsageDataEntity(
                        employeeId = request.employeeId,
                        note = request.note,
                    ),
                    sessionId = session.id.toString(),
                )
            )
            UseSessionResponse(
                session = with(sessionsConverter) { session.toData(sessionsRepository.findUsagesForSession(session.id.toString())) }
            )
        }
    }

    private suspend fun getConfiguredServiceById(serviceId: String, configurationId: String): ConfiguredService {
        val service = servicesRepository.findServiceById(serviceId)
        val configuration = servicesRepository.findConfigurationForService(configurationId, serviceId)
        return ConfiguredService(
            serviceId = serviceId,
            info = with(servicesConverter) { service.info.toData() },
            image = with(assetsConverter) { service.image?.toData() },
            configuration = with(servicesConverter) { configuration.toData() },
        )
    }


    override suspend fun getSessionsStats(request: GetSessionsStatsRequest): GetSessionsStatsResponse {
        return coroutineScope {
            val activatedSessionsCount = async { sessionsRepository.countActivationsForPeriod(request.period) }
            val usedSessionsCount = async { sessionsRepository.countUsagesForPeriod(request.period) }
            val usagesForPeriod = async { sessionsRepository.findUsagesForPeriod(request.period) }
            val popularService = async {
                val mostPopularService = usagesForPeriod.await().map {
                    async {
                        sessionsRepository.findSessionById(it.sessionId).activation.configuration
                    }
                }.awaitAll().groupBy { it.serviceId }.values.maxByOrNull {
                    it.size
                } ?: return@async null
                val service = mostPopularService.first()
                MostPopularService(
                    count = mostPopularService.size,
                    configuredService = with(servicesConverter) {
                        servicesRepository.findServiceById(service.serviceId).toData()
                    }
                )
            }
            val usages = async {
                usagesForPeriod.await().groupBy { it.data.date.toLocalDate() }
                    .map { DayToCountUsageStat(it.key, it.value.size) }
            }
            return@coroutineScope GetSessionsStatsResponse(
                stats = SessionStats(
                    activatedSessionsCount = activatedSessionsCount.await(),
                    usedSessionsCount = usedSessionsCount.await(),
                    popularService = popularService.await() ?: TODO(),
                    usages = usages.await(),
                ),
                period = request.period
            )
        }

    }

    override suspend fun deactivateSession(request: DeactivateSessionRequest): DeactivateSessionResponse {
        val session = sessionsRepository.deactivateSessionForCustomer(request.sessionId, request.data)
        val usages = sessionsRepository.findUsagesForSession(request.sessionId)
        return DeactivateSessionResponse(
            with(sessionsConverter) { session.toData(usages) }
        )
    }


}