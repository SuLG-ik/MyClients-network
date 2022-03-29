package beauty.shafran.network.customers.repository

import beauty.shafran.network.customers.converters.CardTokenDecoder
import beauty.shafran.network.customers.converters.CardTokenEncoder
import beauty.shafran.network.customers.converters.CustomersConverter
import beauty.shafran.network.customers.data.*
import beauty.shafran.network.customers.entity.CardEntity
import beauty.shafran.network.customers.entity.CustomerDataEntity
import beauty.shafran.network.customers.entity.CustomerEntity
import beauty.shafran.network.customers.exceptions.CustomersException
import beauty.shafran.network.phone.entity.PhoneNumberEntity
import beauty.shafran.network.session.repository.ServiceSessionsRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList
import org.bson.types.ObjectId
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.litote.kmongo.coroutine.CoroutineDatabase
import org.litote.kmongo.div
import org.litote.kmongo.eq
import org.litote.kmongo.id.WrappedObjectId

class MongoCustomersRepository(
    client: CoroutineDatabase,
    private val tokenEncoder: CardTokenEncoder,
    private val tokenDecoder: CardTokenDecoder,
    private val converter: CustomersConverter,
) : CustomersRepository, KoinComponent {

    private val customers = client.getCollection<CustomerEntity>("customers")
    private val cards = client.getCollection<CardEntity>("cards")
    private val sessionsRepository by inject<ServiceSessionsRepository>()


    override suspend fun searchCustomerByPhone(data: SearchCustomerByPhoneRequest): SearchCustomerByPhoneResponse {
        val customer = findCustomerByPhoneNumber(data.phoneNumber.number)
        return coroutineScope {
            SearchCustomerByPhoneResponse(
                searchResult = customer.mapNotNull { with(converter) { it.toData() as? Customer.ActivatedCustomer } }
                    .map {
                        async {
                            FoundCustomerItem(
                                customer = it,
                                lastUsedSession = sessionsRepository.getLastSessionForCustomer(it.id)
                            )
                        }
                    }.awaitAll()
            )
        }
    }

    override suspend fun restoreCustomer(request: RestoreCustomerRequest) {
        val customer = CustomerEntity()
        customers.insertOne(customer)
        val token = tokenDecoder.decodeTokenToId(request.cardId)
        cards.save(CardEntity(customer.id.toString(), WrappedObjectId(token)))
    }

    override suspend fun createCustomer(request: CreateCustomersRequest): CreateCustomersResponse {
        val customerData = with(converter) { request.data.toNewEntity() }
        val customer = CustomerEntity(customerData)
        customers.insertOne(customer)
        val card = CardEntity(customer.id.toString())
        cards.insertOne(card)
        return CreateCustomersResponse(
            token = tokenEncoder.encodeTokenByCard(card),
            customer = with(converter) { customer.toData() as Customer.ActivatedCustomer }
        )
    }

    override suspend fun getCustomerById(request: GetCustomerByIdRequest): GetCustomerByIdResponse {
        val customer = findCustomerById(request.id)
        val card = findCardByCustomerId(customer.id.toString())
        return GetCustomerByIdResponse(
            cardToken = tokenEncoder.encodeTokenByCard(card),
            customer = with(converter) { customer.toData() }
        )
    }

    override suspend fun getCustomerByToken(request: GetCustomerByTokenRequest): GetCustomerByTokenResponse {
        val id = tokenDecoder.decodeTokenToId(request.token)
        val card = findCardById(id)
        val customer = findCustomerById(card.customerId)
        return GetCustomerByTokenResponse(
            cardToken = tokenEncoder.encodeTokenByCard(card),
            customer = with(converter) { customer.toData() }
        )
    }


    override suspend fun getAllCustomers(request: GetAllCustomersRequest): GetAllCustomersResponse {
        val customers = customers.find().toFlow().map {
            with(converter) { it.toData() }
        }
        return GetAllCustomersResponse(
            customers = customers.toList()
        )
    }

    override suspend fun createEmptyCustomers(request: CreateEmptyCustomersRequest): CreateEmptyCustomersResponse {
        val customers = List(request.count) { CustomerEntity() }
        this.customers.insertMany(customers)
        val cards = customers.associateBy { CardEntity(it.id.toString()) }
        this.cards.insertMany(cards.keys.toList())
        return CreateEmptyCustomersResponse(
            cards = cards.map { tokenEncoder.encodeTokenByCard(it.key) to with(converter) { it.value.toData() as Customer.InactivatedCustomer } }
                .toMap()
        )
    }

    override suspend fun editCustomerData(request: EditCustomerRequest): EditCustomerResponse {
        val oldEntity = findCustomerById(request.customerId)
        val newEntity = oldEntity.copy(
            data = with(converter) { request.data.toNewEntity() }
        )
        customers.save(newEntity)
        return EditCustomerResponse(
            customer = with(converter) { newEntity.toData() as Customer.ActivatedCustomer }
        )
    }

    private suspend fun findCardById(token: String): CardEntity {
        return cards.findOneById(ObjectId(token)) ?: throw CustomersException.CardNotFoundWithIdException(token)
    }

    private suspend fun findCardByCustomerId(customerId: String): CardEntity {
        return cards.findOne(CardEntity::customerId eq customerId)
            ?: throw CustomersException.CardNotFoundWithCustomerException(customerId)
    }

    private suspend fun findCustomerById(customerId: String): CustomerEntity {
        return customers.findOneById(ObjectId(customerId))
            ?: throw CustomersException.CustomerNotFoundException(customerId)
    }


    private suspend fun findCustomerByPhoneNumber(number: String): List<CustomerEntity> {
        return customers.find(CustomerEntity::data / CustomerDataEntity::phone / PhoneNumberEntity::number eq number)
            .toList()
    }

}