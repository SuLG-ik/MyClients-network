package beauty.shafran.network.session.executor

import beauty.shafran.network.assets.converter.AssetsConverter
import beauty.shafran.network.auth.AuthorizationValidator
import beauty.shafran.network.auth.data.AuthorizedAccount
import beauty.shafran.network.customers.converters.CustomersConverter
import beauty.shafran.network.customers.data.Customer
import beauty.shafran.network.customers.data.CustomerId
import beauty.shafran.network.customers.repository.CustomersRepository
import beauty.shafran.network.services.converter.ServicesConverter
import beauty.shafran.network.services.data.ConfiguredService
import beauty.shafran.network.services.data.ServiceConfigurationId
import beauty.shafran.network.services.repository.ServicesRepository
import beauty.shafran.network.session.converters.SessionsConverter
import beauty.shafran.network.session.data.*
import beauty.shafran.network.session.entity.ServiceSessionActivationEntity
import beauty.shafran.network.session.entity.ServiceSessionUsageDataEntity
import beauty.shafran.network.session.entity.ServiceSessionUsageEntity
import beauty.shafran.network.session.repository.ServiceSessionsRepository
import beauty.shafran.network.utils.MetaEntity
import beauty.shafran.network.utils.Transactional
import beauty.shafran.network.utils.TransactionalScope
import kotlinx.coroutines.awaitAll
import org.koin.core.annotation.Single

@Single
class SessionsExecutorImpl(
    private val sessionsConverter: SessionsConverter,
    private val customersConverter: CustomersConverter,
    private val assetsConverter: AssetsConverter,
    private val servicesConverter: ServicesConverter,
    private val serviceSessionsRepository: ServiceSessionsRepository,
    private val servicesRepository: ServicesRepository,
    private val customersRepository: CustomersRepository,
    private val auth: AuthorizationValidator,
    private val transactional: Transactional,
) : SessionsExecutor {


    override suspend fun getSessionUsagesHistory(
        request: GetSessionUsagesHistoryRequest,
        account: AuthorizedAccount,
    ): GetSessionUsagesHistoryResponse {
        return transactional.withSuspendedTransaction {
            val lastUsages = with(serviceSessionsRepository) { findUsagesHistory(request.paged, request.storageId) }
            GetSessionUsagesHistoryResponse(
                usages = lastUsages.mapNotNull { loadSessionUsageHistoryItem(it) },
                paged = request.paged,
            )

        }
    }

    private suspend fun loadSessionUsageHistoryItem(
        entity: ServiceSessionUsageEntity,
    ): SessionUsageHistoryItem? {
        return transactional.withSuspendedTransaction {
            val usage = transactionAsync { with(sessionsConverter) { toData(entity) } }
            val session = transactionAsync { with(serviceSessionsRepository) { findSessionById(entity.sessionId) } }
            val configuration = transactionAsync {
                with(servicesRepository) {
                    findServiceConfiguration(
                        session.await().activation.configuration,
                    )
                }
            }
            val service =
                transactionAsync { with(servicesRepository) { findServiceById(configuration.await().serviceId) } }
            SessionUsageHistoryItem(
                service = ConfiguredService(serviceId = service.await().id,
                    info = with(servicesConverter) { service.await().info.toData() },
                    image = with(assetsConverter) { service.await().image?.toData() },
                    configuration = with(servicesConverter) { configuration.await().toData() }),
                usage = usage.await(),
                customer = loadCustomer(session.await().activation.customerId)
                    ?: return@withSuspendedTransaction null
            )
        }

    }

    private suspend fun TransactionalScope.loadCustomer(customerId: CustomerId): Customer {
        val customer = transactionAsync { customersRepository.findCustomerById(customerId) }
        val data = transactionAsync { customersRepository.findCustomerDataById(customerId) }
        return customersConverter.buildCustomer(customer.await(), data.await())
    }

    override suspend fun getAllSessionsForCustomer(
        request: GetSessionsForCustomerRequest,
        account: AuthorizedAccount,
    ): GetSessionsForCustomerResponse {
        return transactional.withSuspendedTransaction {
            with(customersRepository) { throwIfCustomerNotExists(request.customerId) }
            val sessionEntities =
                with(serviceSessionsRepository) { findSessionsForCustomer(request.customerId, request.storageId) }
            GetSessionsForCustomerResponse(serviceSessions = sessionEntities.map {
                with(sessionsConverter) {
                    transactionAsync {
                        toData(it, with(serviceSessionsRepository) { findUsagesForSession(it.id) })
                    }
                }
            }.toList().awaitAll())
        }
    }

    override suspend fun getSessionsIgnoreDeactivatedForCustomer(
        request: GetSessionsForCustomerRequest,
        account: AuthorizedAccount,
    ): GetSessionsForCustomerResponse {
        return transactional.withSuspendedTransaction {
            with(customersRepository) { throwIfCustomerNotExists(request.customerId) }
            val sessionEntities = with(serviceSessionsRepository) {
                findSessionsIgnoreDeactivatedForCustomer(
                    request.customerId,
                    request.storageId
                )
            }
            GetSessionsForCustomerResponse(serviceSessions = sessionEntities.map {
                with(sessionsConverter) {
                    transactionAsync {
                        toData(it, with(serviceSessionsRepository) {
                            findUsagesForSession(it.id)
                        })
                    }
                }
            }.toList().awaitAll())
        }
    }

    override suspend fun createSessionsForCustomer(
        request: CreateSessionForCustomerRequest,
        account: AuthorizedAccount,
    ): CreateSessionForCustomerResponse {
        return transactional.withSuspendedTransaction {
            with(customersRepository) { throwIfCustomerNotExists(request.customerId) }
            val session = with(serviceSessionsRepository) {
                insertSession(
                    activation = ServiceSessionActivationEntity(
                        configuration = request.configurationId,
                        customerId = request.customerId,
                        employeeId = request.employeeId,
                        note = request.data.note.orEmpty(),
                        stationId = request.stationId,
                    ),
                    storageId = request.storageId,
                )
            }
            CreateSessionForCustomerResponse(serviceSession = with(sessionsConverter) {
                toData(session, with(serviceSessionsRepository) { findUsagesForSession(session.id) })
            })
        }
    }

    override suspend fun useSession(request: UseSessionRequest, account: AuthorizedAccount): UseSessionResponse {
        return transactional.withSuspendedTransaction {
            val session = with(serviceSessionsRepository) { findSessionById(request.sessionId) }
            with(serviceSessionsRepository) {
                useSession(
                    data = ServiceSessionUsageDataEntity(
                        employeeId = request.employeeId,
                        note = request.note.orEmpty(),
                        meta = MetaEntity()
                    ),
                    sessionId = request.sessionId,
                    stationId = request.stationId,
                )
            }
            UseSessionResponse(serviceSession = with(sessionsConverter) {
                toData(session, with(serviceSessionsRepository) { findUsagesForSession(session.id) })
            })
        }
    }

    private suspend fun getConfiguredServiceById(
        configurationId: ServiceConfigurationId,
    ): ConfiguredService {
        return transactional.withSuspendedTransaction {
            val configuration = with(servicesRepository) { findServiceConfiguration(configurationId) }
            val service = with(servicesRepository) { findServiceById(configuration.serviceId) }
            ConfiguredService(
                serviceId = service.id,
                info = with(servicesConverter) { service.info.toData() },
                image = with(assetsConverter) { service.image?.toData() },
                configuration = with(servicesConverter) { configuration.toData() },
            )
        }
    }


    override suspend fun getSessionsStats(
        request: GetSessionsStatsRequest,
        account: AuthorizedAccount,
    ): GetSessionsStatsResponse {
        return transactional.withSuspendedTransaction {
            val activatedSessionsCount =
                transactionAsync {
                    with(serviceSessionsRepository) {
                        countActivationsForPeriod(
                            request.period,
                            request.storageId
                        )
                    }
                }
            val usedSessionsCount =
                transactionAsync {
                    with(serviceSessionsRepository) {
                        countUsagesForPeriod(
                            request.period,
                            request.storageId
                        )
                    }
                }
            val usagesForPeriod =
                transactionAsync {
                    with(serviceSessionsRepository) {
                        findUsagesForPeriod(
                            request.period,
                            request.storageId
                        )
                    }
                }
            val popularService = transactionAsync {
                val mostPopularService = usagesForPeriod.await().map {
                    transactionAsync {
                        with(serviceSessionsRepository) { findSessionById(it.sessionId) }.activation.configuration
                    }
                }.awaitAll().groupBy { it.id }.values.maxByOrNull {
                    it.size
                } ?: return@transactionAsync null
                val configurationId = mostPopularService.first()
                val service = with(servicesRepository) { findServiceByConfigurationId(configurationId) }
                MostPopularService(count = mostPopularService.size, configuredService = with(servicesConverter) {
                    service.toData()
                })
            }
            val usages = transactionAsync {
                usagesForPeriod.await().groupBy { it.data.meta.creationDate }
                    .map { DayToCountUsageStat(it.key.date, it.value.size) }
            }
            GetSessionsStatsResponse(
                stats = SessionStats(
                    activatedSessionsCount = activatedSessionsCount.await(),
                    usedSessionsCount = usedSessionsCount.await(),
                    popularService = popularService.await() ?: TODO(),
                    usages = usages.await(),
                ), period = request.period
            )
        }

    }

    override suspend fun deactivateSession(
        request: DeactivateSessionRequest,
        account: AuthorizedAccount,
    ): DeactivateSessionResponse {
        return transactional.withSuspendedTransaction {
            val newSession =
                with(serviceSessionsRepository) { deactivateSessionForCustomer(request.sessionId, request.data) }
            val usages = with(serviceSessionsRepository) { findUsagesForSession(request.sessionId) }
            DeactivateSessionResponse(with(sessionsConverter) { toData(newSession, usages) })
        }
    }


}