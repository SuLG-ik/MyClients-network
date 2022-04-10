package beauty.shafran.network.customers.repository

import beauty.shafran.network.CardNotExistsForCustomer
import beauty.shafran.network.CardNotExistsWithId
import beauty.shafran.network.CustomerNotExists
import beauty.shafran.network.customers.entity.CardEntity
import beauty.shafran.network.customers.entity.CustomerDataEntity
import beauty.shafran.network.customers.entity.CustomerEntity
import beauty.shafran.network.customers.entity.collectionName
import beauty.shafran.network.phone.entity.PhoneNumberEntity
import beauty.shafran.network.utils.paged
import beauty.shafran.network.utils.toIdSecure
import org.litote.kmongo.coroutine.CoroutineDatabase
import org.litote.kmongo.coroutine.updateOne
import org.litote.kmongo.div
import org.litote.kmongo.eq
import org.litote.kmongo.ne
import org.springframework.stereotype.Repository

@Repository
class MongoCustomersRepository(
    coroutineDatabase: CoroutineDatabase,
) : CustomersRepository {

    private val customersCollection = coroutineDatabase.getCollection<CustomerEntity>(CustomerEntity.collectionName)
    private val cardsCollection = coroutineDatabase.getCollection<CardEntity>(CardEntity.collectionName)

    override suspend fun throwIfCustomerNotExists(customerId: String) {
        if (!isCustomerExists(customerId)) throw CustomerNotExists(customerId)
    }

    override suspend fun isCustomerExists(customerId: String): Boolean {
        return customersCollection.countDocuments(CustomerEntity::id eq customerId.toIdSecure("customerId")) >= 1
    }

    override suspend fun findCustomerById(customerId: String): CustomerEntity {
        return customersCollection.findOneById(customerId.toIdSecure<CustomerEntity>("customerId"))
            ?: throw CustomerNotExists(customerId)
    }

    override suspend fun findCustomerByIdOrNull(customerId: String): CustomerEntity? {
        return customersCollection.findOneById(customerId.toIdSecure<CustomerEntity>("customerId"))
    }

    override suspend fun updateCustomerData(customerId: String, data: CustomerDataEntity): CustomerEntity {
        return findCustomerById(customerId).copy(data = data).also { customersCollection.updateOne(it) }
    }

    override suspend fun insertCustomer(data: CustomerDataEntity?): CustomerEntity {
        return CustomerEntity(data = data).also { customersCollection.insertOne(it) }
    }

    override suspend fun insertCustomers(count: Int): List<CustomerEntity> {
        return List(count) { CustomerEntity() }.also { customersCollection.insertMany(it) }
    }

    override suspend fun insertCard(customerId: String): CardEntity {
        if (!isCustomerExists(customerId))
            throw CustomerNotExists(customerId)
        return CardEntity(customerId = customerId).also { cardsCollection.insertOne(it) }
    }

    override suspend fun insertCard(customerId: String, cardId: String): CardEntity {
        if (!isCustomerExists(customerId))
            throw CustomerNotExists(customerId)
        return CardEntity(customerId = customerId,
            id = cardId.toIdSecure("cardId")).also { cardsCollection.insertOne(it) }
    }

    override suspend fun insertCards(customers: List<CustomerEntity>): Map<CardEntity, CustomerEntity> {
        val cards = customers.associateBy { CardEntity(customerId = it.id.toString()) }
        cardsCollection.insertMany(cards.keys.toList())
        return cards
    }

    override suspend fun findCardById(cardId: String): CardEntity {
        return cardsCollection.findOneById(cardId.toIdSecure<CardEntity>("cardId")) ?: throw CardNotExistsWithId(cardId)
    }

    override suspend fun findCardByCustomerId(customerId: String): CardEntity {
        return cardsCollection.findOne(CardEntity::customerId eq customerId) ?: throw CardNotExistsForCustomer(
            customerId)
    }

    override suspend fun findCustomerByPhoneNumber(number: String): List<CustomerEntity> {
        return customersCollection.find(CustomerEntity::data / CustomerDataEntity::phone / PhoneNumberEntity::number eq number)
            .toList()
    }

    override suspend fun findAllCustomers(offset: Int, page: Int): List<CustomerEntity> {
        return customersCollection.find(CustomerEntity::data ne null)
            .ascendingSort(CustomerEntity::data / CustomerDataEntity::name)
            .paged(offset, page)
            .toList()
    }

}