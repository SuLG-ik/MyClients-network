package beauty.shafran.network.session.executor

import beauty.shafran.SessionIsDeactivated
import beauty.shafran.SessionOveruseException
import beauty.shafran.network.assets.converter.AssetsConverter
import beauty.shafran.network.auth.AuthorizationValidator
import beauty.shafran.network.auth.data.AuthorizedAccount
import beauty.shafran.network.auth.throwIfNotAccessedForCompany
import beauty.shafran.network.companies.data.CompanyId
import beauty.shafran.network.companies.entity.AccessScope
import beauty.shafran.network.customers.converters.CustomersConverter
import beauty.shafran.network.customers.data.Customer
import beauty.shafran.network.customers.repository.CustomersRepository
import beauty.shafran.network.services.converter.ServicesConverter
import beauty.shafran.network.services.data.ConfiguredService
import beauty.shafran.network.services.enity.toCompanyId
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
import org.koin.core.annotation.Single

@Single
class SessionsExecutorImpl(
    private val sessionsConverter: SessionsConverter,
    private val customersConverter: CustomersConverter,
    private val assetsConverter: AssetsConverter,
    private val servicesConverter: ServicesConverter,
    private val sessionsRepository: SessionsRepository,
    private val servicesRepository: ServicesRepository,
    private val customersRepository: CustomersRepository,
    private val auth: AuthorizationValidator,
) : SessionsExecutor {


    override suspend fun getSessionUsagesHistory(
        request: GetSessionUsagesHistoryRequest,
        account: AuthorizedAccount,
    ): GetSessionUsagesHistoryResponse {
        auth.throwIfNotAccessedForCompany(CompanyId(request.companyId), AccessScope.SESSIONS_READ, account)
        val lastUsages = sessionsRepository.findUsagesHistory(
            offset = request.offset,
            page = request.page,
            companyId = request.companyId
        )
        return coroutineScope {
            GetSessionUsagesHistoryResponse(
                usages = lastUsages.mapNotNull { loadSessionUsageHistoryItem(it) },
                offset = request.offset,
                page = request.page,
            )
        }
    }

    private suspend fun loadSessionUsageHistoryItem(
        entity: SessionUsageEntity,
    ): SessionUsageHistoryItem? {
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

    override suspend fun getAllSessionsForCustomer(
        request: GetSessionsForCustomerRequest,
        account: AuthorizedAccount,
    ): GetSessionsForCustomerResponse {
        return coroutineScope {
            val customer = customersRepository.findCustomerById(request.customerId)
            auth.throwIfNotAccessedForCompany(customer.companyReference.toCompanyId(), AccessScope.SESSIONS_READ, account)
            val sessionEntities = sessionsRepository.findSessionsForCustomer(request.customerId)
            GetSessionsForCustomerResponse(
                sessions = sessionEntities.map {
                    with(sessionsConverter) { async { it.toData(sessionsRepository.findUsagesForSession(it.id.toString())) } }
                }.toList().awaitAll()
            )
        }
    }

    override suspend fun getSessionsIgnoreDeactivatedForCustomer(
        request: GetSessionsForCustomerRequest,
        account: AuthorizedAccount,
    ): GetSessionsForCustomerResponse {
        return coroutineScope {
            val customer = customersRepository.findCustomerById(request.customerId)
            auth.throwIfNotAccessedForCompany(customer.companyReference.toCompanyId(), AccessScope.SESSIONS_READ, account)
            val sessionEntities = sessionsRepository.findSessionsIgnoreDeactivatedForCustomer(request.customerId)
            GetSessionsForCustomerResponse(
                sessions = sessionEntities.map {
                    with(sessionsConverter) { async { it.toData(sessionsRepository.findUsagesForSession(it.id.toString())) } }
                }.toList().awaitAll()
            )
        }
    }

    override suspend fun createSessionsForCustomer(
        request: CreateSessionForCustomerRequest,
        account: AuthorizedAccount,
    ): CreateSessionForCustomerResponse {
        return coroutineScope {
            val customer = customersRepository.findCustomerById(request.customerId)
            auth.throwIfNotAccessedForCompany(customer.companyReference.toCompanyId(), AccessScope.SESSIONS_ADD, account)
            val session = sessionsRepository.insertSession(
                with(sessionsConverter) { request.toNewEntity() },
                customer.companyReference.companyId,
            )
            CreateSessionForCustomerResponse(
                session = with(sessionsConverter) { session.toData(sessionsRepository.findUsagesForSession(session.id.toString())) }
            )
        }
    }

    override suspend fun useSession(request: UseSessionRequest, account: AuthorizedAccount): UseSessionResponse {
        return coroutineScope {
            val session =
                sessionsRepository.findSessionById(request.sessionId)
            auth.throwIfNotAccessedForCompany(session.companyReference.toCompanyId(), AccessScope.SESSIONS_ADD, account)
            if (session.deactivation != null)
                throw SessionIsDeactivated(request.sessionId)
            val configuration = async {
                servicesRepository.findConfigurationForService(
                    session.activation.configuration.configurationId,
                    session.activation.configuration.serviceId
                )
            }
            val usagesCount =
                async { sessionsRepository.countUsagesForSession(sessionId = request.sessionId) }

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
                    companyReference = session.companyReference,
                )
            )
            UseSessionResponse(
                session = with(sessionsConverter) { session.toData(sessionsRepository.findUsagesForSession(session.id.toString())) }
            )
        }
    }

    private suspend fun getConfiguredServiceById(
        serviceId: String,
        configurationId: String,
    ): ConfiguredService {
        val service = servicesRepository.findServiceById(serviceId)
        val configuration = servicesRepository.findConfigurationForService(configurationId, serviceId)
        return ConfiguredService(
            serviceId = serviceId,
            info = with(servicesConverter) { service.info.toData() },
            image = with(assetsConverter) { service.image?.toData() },
            configuration = with(servicesConverter) { configuration.toData() },
        )
    }


    override suspend fun getSessionsStats(
        request: GetSessionsStatsRequest,
        account: AuthorizedAccount,
    ): GetSessionsStatsResponse {
        return coroutineScope {
            val activatedSessionsCount =
                async { sessionsRepository.countActivationsForPeriod(request.period, request.companyId) }
            val usedSessionsCount = async { sessionsRepository.countUsagesForPeriod(request.period, request.companyId) }
            val usagesForPeriod = async { sessionsRepository.findUsagesForPeriod(request.period, request.companyId) }
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

    override suspend fun deactivateSession(
        request: DeactivateSessionRequest,
        account: AuthorizedAccount,
    ): DeactivateSessionResponse {
        val session = sessionsRepository.findSessionById(request.sessionId)
        auth.throwIfNotAccessedForCompany(session.companyReference.toCompanyId(), AccessScope.SESSIONS_REMOVE, account)
        val newSession = sessionsRepository.deactivateSessionForCustomer(request.sessionId, request.data)
        val usages = sessionsRepository.findUsagesForSession(request.sessionId)
        return DeactivateSessionResponse(
            with(sessionsConverter) { newSession.toData(usages) }
        )
    }


}