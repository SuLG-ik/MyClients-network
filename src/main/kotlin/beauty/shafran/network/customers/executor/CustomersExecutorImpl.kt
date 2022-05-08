package beauty.shafran.network.customers.executor

import beauty.shafran.CustomerNotExists
import beauty.shafran.network.auth.AuthorizationValidator
import beauty.shafran.network.auth.data.AuthorizedAccount
import beauty.shafran.network.auth.throwIfNotAccessedForCompany
import beauty.shafran.network.companies.data.CompanyId
import beauty.shafran.network.companies.entity.AccessScope
import beauty.shafran.network.customers.converters.CardTokenDecoder
import beauty.shafran.network.customers.converters.CardTokenEncoder
import beauty.shafran.network.customers.converters.CustomersConverter
import beauty.shafran.network.customers.data.*
import beauty.shafran.network.customers.repository.CustomersRepository
import beauty.shafran.network.session.converters.SessionsConverter
import beauty.shafran.network.session.repository.SessionsRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import org.koin.core.annotation.Single

@Single
class CustomersExecutorImpl(
    private val tokenEncoder: CardTokenEncoder,
    private val tokenDecoder: CardTokenDecoder,
    private val customersConverter: CustomersConverter,
    private val sessionsConverter: SessionsConverter,
    private val sessionsRepository: SessionsRepository,
    private val customersRepository: CustomersRepository,
    private val auth: AuthorizationValidator,
) : CustomersExecutor {


    override suspend fun searchCustomerByPhone(
        request: SearchCustomerByPhoneRequest,
        account: AuthorizedAccount,
    ): SearchCustomerByPhoneResponse {
        auth.throwIfNotAccessedForCompany(request.companyId, AccessScope.CUSTOMERS_READ, account)
        val customer =
            customersRepository.findCustomerByPhoneNumber(request.phoneNumber.number, companyId = request.companyId.id)
        return coroutineScope {
            SearchCustomerByPhoneResponse(
                searchResult = customer.mapNotNull { with(customersConverter) { it.toData() as? Customer.ActivatedCustomer } }
                    .map { customer ->
                        async {
                            FoundCustomerItem(customer = customer,
                                lastUsedSession = runCatching { sessionsRepository.findSessionById(customer.id) }.getOrNull()
                                    ?.run { with(sessionsConverter) { toData(sessionsRepository.findUsagesForSession(id.toString())) } })
                        }
                    }.awaitAll()
            )
        }
    }

    override suspend fun createCustomer(
        request: CreateCustomersRequest,
        account: AuthorizedAccount,
    ): CreateCustomerResponse {
        auth.throwIfNotAccessedForCompany(
            companyId = request.companyId,
            scope = AccessScope.CUSTOMERS_ADD,
            account = account,
        )
        val customerData = with(customersConverter) { request.data.toNewEntity() }
        val customer = customersRepository.insertCustomer(customerData, companyId = request.companyId.id)
        val card = customersRepository.insertCard(customer.id.toString(), companyId = request.companyId.id)
        return CreateCustomerResponse(token = tokenEncoder.encodeTokenByCard(card),
            customer = with(customersConverter) { customer.toData() as Customer.ActivatedCustomer })
    }

    override suspend fun getCustomerById(
        request: GetCustomerByIdRequest,
        account: AuthorizedAccount,
    ): GetCustomerByIdResponse {
        val customer = customersRepository.findCustomerById(request.customerId)
        auth.throwIfNotAccessedForCompany(
            companyId = CompanyId(customer.companyReference.companyId),
            scope = AccessScope.CUSTOMERS_READ,
            account = account,
            cause = { CustomerNotExists(request.customerId) },
        )
        val card = customersRepository.findCardByCustomerId(customer.id.toString())
        auth.throwIfNotAccessedForCompany(
            companyId = CompanyId(card.companyReference.companyId),
            scope = AccessScope.CUSTOMERS_READ,
            account = account,
            cause = { CustomerNotExists(request.customerId) },
        )
        return GetCustomerByIdResponse(
            cardToken = tokenEncoder.encodeTokenByCard(card),
            customer = with(customersConverter) { customer.toData() },
        )
    }

    override suspend fun getCustomerByToken(
        request: GetCustomerByTokenRequest,
        account: AuthorizedAccount,
    ): GetCustomerByTokenResponse {
        val id = tokenDecoder.decodeTokenToId(request.token)
        val card = customersRepository.findCardById(id)
        auth.throwIfNotAccessedForCompany(
            companyId = CompanyId(card.companyReference.companyId),
            scope = AccessScope.CUSTOMERS_READ,
            account = account,
            cause = { CustomerNotExists(request.token) }
        )
        val customer = customersRepository.findCustomerById(card.customerId)
        auth.throwIfNotAccessedForCompany(
            companyId = CompanyId(customer.companyReference.companyId),
            scope = AccessScope.CUSTOMERS_READ,
            account = account,
            cause = { CustomerNotExists(request.token) }
        )
        return GetCustomerByTokenResponse(cardToken = tokenEncoder.encodeTokenByCard(card),
            customer = with(customersConverter) { customer.toData() })
    }

    override suspend fun getAllCustomers(
        request: GetAllCustomersRequest,
        account: AuthorizedAccount,
    ): GetAllCustomersResponse {
        auth.throwIfNotAccessedForCompany(CompanyId(request.companyId), AccessScope.CUSTOMERS_READ, account)
        val customers =
            customersRepository.findAllCustomers(request.offset, request.page, companyId = request.companyId)
                .map { with(customersConverter) { it.toData() } }
        return GetAllCustomersResponse(customers = customers.toList(), page = request.page, offset = request.offset)
    }

    override suspend fun createEmptyCustomers(
        request: CreateEmptyCustomersRequest,
        account: AuthorizedAccount,
    ): CreateEmptyCustomersResponse {
        auth.throwIfNotAccessedForCompany(CompanyId(request.companyId), AccessScope.CUSTOMERS_ADD, account)
        val customers = customersRepository.insertCustomers(request.count, companyId = request.companyId)
        val cards = customersRepository.insertCards(customers, companyId = request.companyId)
        return CreateEmptyCustomersResponse(
            cards = cards.map {
                tokenEncoder.encodeTokenByCard(it.key) to with(customersConverter) { it.value.toData() as Customer.InactivatedCustomer }
            }.toMap()
        )
    }

    override suspend fun editCustomerData(
        request: EditCustomerRequest,
        account: AuthorizedAccount,
    ): EditCustomerDataResponse {
        val customer = customersRepository.findCustomerById(request.customerId)
        auth.throwIfNotAccessedForCompany(
            companyId = CompanyId(customer.companyReference.companyId),
            scope = AccessScope.CUSTOMERS_EDIT,
            account = account,
        )
        val newEntity = customersRepository.updateCustomerData(customerId = request.customerId,
            data = with(customersConverter) { request.data.toNewEntity() })
        return EditCustomerDataResponse(customer = with(customersConverter) { newEntity.toData() as Customer.ActivatedCustomer })
    }

}