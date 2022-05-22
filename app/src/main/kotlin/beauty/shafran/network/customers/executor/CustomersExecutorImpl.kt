package beauty.shafran.network.customers.executor

import beauty.shafran.network.auth.data.AuthorizedAccount
import beauty.shafran.network.cards.data.CardId
import beauty.shafran.network.cards.repository.CardsRepository
import beauty.shafran.network.cards.token.CardTokenDecoder
import beauty.shafran.network.customers.converters.CustomersConverter
import beauty.shafran.network.customers.data.*
import beauty.shafran.network.customers.entity.CustomerEntity
import beauty.shafran.network.customers.repository.CustomersRepository
import beauty.shafran.network.utils.Transactional
import beauty.shafran.network.utils.TransactionalScope
import kotlinx.coroutines.awaitAll
import org.koin.core.annotation.Single

@Single
class CustomersExecutorImpl(
    private val tokenDecoder: CardTokenDecoder,
    private val customersConverter: CustomersConverter,
    private val customersRepository: CustomersRepository,
    private val cardsRepository: CardsRepository,
    private val transactional: Transactional,
) : CustomersExecutor {


    override suspend fun searchCustomerByPhone(
        request: SearchCustomerByPhoneRequest,
        account: AuthorizedAccount,
    ): SearchCustomerByPhoneResponse {
        return transactional.withSuspendedTransaction {
            val customers =
                customersRepository.findCustomerByPhoneNumber(request.phoneNumber.number, request.storageId)
            val customersData = customersRepository.findAllCustomersData(customers.map { CustomerId(it.id) })
            SearchCustomerByPhoneResponse(
                searchResult = customers
                    .mapNotNull { customer ->
                        val customerData = customersData[CustomerId(customer.id)] ?: return@mapNotNull null
                        transactionAsync {
                            FoundCustomerItem(
                                customer = customersConverter.buildCustomer(customer, customerData),
                            )
                        }
                    }.awaitAll()
            )
        }
    }


    override suspend fun createCustomer(
        request: CreateCustomersRequest,
        account: AuthorizedAccount,
    ): CreateCustomerResponse {
        return transactional.withSuspendedTransaction {
            val customerEntity = customersRepository.insertCustomer(request.data, storageId = request.storageId)
            CreateCustomerResponse(
                customer = loadCustomer(customerEntity)
            )
        }
    }

    override suspend fun getCustomerById(
        request: GetCustomerByIdRequest,
        account: AuthorizedAccount,
    ): GetCustomerByIdResponse {
        return transactional.withSuspendedTransaction {
            val customer = loadCustomer(request.customerId)
            GetCustomerByIdResponse(
                customer = customer,
            )
        }
    }

    override suspend fun getCustomerByToken(
        request: GetCustomerByTokenRequest,
        account: AuthorizedAccount,
    ): GetCustomerByTokenResponse {
        return transactional.withSuspendedTransaction {
            val token = tokenDecoder.decodeRawToken(request.rawToken)
            val card = cardsRepository.findCardByToken(token)
            val customerId = cardsRepository.findConnectedCustomerToCard(CardId(card.id))
            GetCustomerByTokenResponse(
                customer = loadCustomer(customerId)
            )
        }
    }

    override suspend fun getAllCustomers(
        request: GetAllCustomersRequest,
        account: AuthorizedAccount,
    ): GetAllCustomersResponse {
        return transactional.withSuspendedTransaction {
            val customers = customersRepository.findAllCustomers(request.paged, request.storageId)
            val data = customersRepository.findAllCustomersData(customers.map { CustomerId(it.id) })
            GetAllCustomersResponse(
                customers = customers.mapNotNull {
                    val customerData = data[CustomerId(it.id)] ?: return@mapNotNull null
                    customersConverter.buildCustomer(it, customerData)
                },
                paged = request.paged,
            )
        }
    }

    private suspend fun TransactionalScope.loadCustomer(customer: CustomerEntity): Customer {
        val data = customersRepository.findCustomerDataById(CustomerId(customer.id))
        return customersConverter.buildCustomer(customer, data)
    }

    private suspend fun TransactionalScope.loadCustomer(customerId: CustomerId): Customer {
        val customer = transactionAsync { customersRepository.findCustomerById(customerId) }
        val data = transactionAsync { customersRepository.findCustomerDataById(customerId) }
        return customersConverter.buildCustomer(customer.await(), data.await())
    }

    override suspend fun editCustomerData(
        request: EditCustomerRequest,
        account: AuthorizedAccount,
    ): EditCustomerDataResponse {
        return transactional.withSuspendedTransaction {
            customersRepository.updateCustomerData(
                customerId = request.customerId,
                data = request.data,
            )
            EditCustomerDataResponse(customer = loadCustomer(request.customerId))
        }
    }

}