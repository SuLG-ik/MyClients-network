package beauty.shafran.network.customers.executor

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
import org.springframework.stereotype.Service

@Service
class CustomersExecutorImpl(
    private val tokenEncoder: CardTokenEncoder,
    private val tokenDecoder: CardTokenDecoder,
    private val customersConverter: CustomersConverter,
    private val sessionsConverter: SessionsConverter,
    private val sessionsRepository: SessionsRepository,
    private val customersRepository: CustomersRepository,
) : CustomersExecutor {


    override suspend fun searchCustomerByPhone(request: SearchCustomerByPhoneRequest): SearchCustomerByPhoneResponse {
        val customer = customersRepository.findCustomerByPhoneNumber(request.phoneNumber.number)
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

    override suspend fun restoreCustomer(request: RestoreCustomerRequest) {
        val customer = customersRepository.insertCustomer(null)
        val cardId = tokenDecoder.decodeTokenToId(request.token)
        customersRepository.insertCard(customer.id.toString(), cardId)
    }

    override suspend fun createCustomer(request: CreateCustomersRequest): CreateCustomersResponse {
        val customerData = with(customersConverter) { request.data.toNewEntity() }
        val customer = customersRepository.insertCustomer(customerData)
        val card = customersRepository.insertCard(customer.id.toString())
        return CreateCustomersResponse(token = tokenEncoder.encodeTokenByCard(card),
            customer = with(customersConverter) { customer.toData() as Customer.ActivatedCustomer })
    }

    override suspend fun getCustomerById(request: GetCustomerByIdRequest): GetCustomerByIdResponse {
        val customer = customersRepository.findCustomerById(request.id)
        val card = customersRepository.findCardByCustomerId(customer.id.toString())
        return GetCustomerByIdResponse(cardToken = tokenEncoder.encodeTokenByCard(card),
            customer = with(customersConverter) { customer.toData() })
    }

    override suspend fun getCustomerByToken(request: GetCustomerByTokenRequest): GetCustomerByTokenResponse {
        val id = tokenDecoder.decodeTokenToId(request.token)
        val card = customersRepository.findCardById(id)
        val customer = customersRepository.findCustomerById(card.customerId)
        return GetCustomerByTokenResponse(cardToken = tokenEncoder.encodeTokenByCard(card),
            customer = with(customersConverter) { customer.toData() })
    }

    override suspend fun getAllCustomers(request: GetAllCustomersRequest): GetAllCustomersResponse {
        val customers = customersRepository.findAllCustomers(request.offset, request.page)
            .map { with(customersConverter) { it.toData() } }
        return GetAllCustomersResponse(customers = customers.toList(), page = request.page, offset = request.offset)
    }

    override suspend fun createEmptyCustomers(request: CreateEmptyCustomersRequest): CreateEmptyCustomersResponse {
        val customers = customersRepository.insertCustomers(request.count)
        val cards = customersRepository.insertCards(customers)
        return CreateEmptyCustomersResponse(
            cards = cards.map {
                tokenEncoder.encodeTokenByCard(it.key) to with(customersConverter) { it.value.toData() as Customer.InactivatedCustomer }
            }.toMap()
        )
    }

    override suspend fun editCustomerData(request: EditCustomerRequest): EditCustomerResponse {
        val newEntity = customersRepository.updateCustomerData(customerId = request.customerId,
            data = with(customersConverter) { request.data.toNewEntity() })
        return EditCustomerResponse(customer = with(customersConverter) { newEntity.toData() as Customer.ActivatedCustomer })
    }

}