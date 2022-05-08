package beauty.shafran.network.customers.repository

import beauty.shafran.CardNotExistsForCustomer
import beauty.shafran.CardNotExistsWithId
import beauty.shafran.CustomerNotExists
import beauty.shafran.network.companies.entity.CompanyReferenceEntity
import beauty.shafran.network.customers.entity.CardEntity
import beauty.shafran.network.customers.entity.CustomerDataEntity
import beauty.shafran.network.customers.entity.CustomerEntity
import beauty.shafran.network.customers.entity.collectionName
import beauty.shafran.network.phone.entity.PhoneNumberEntity
import beauty.shafran.network.utils.paged
import beauty.shafran.network.utils.toIdSecure
import org.koin.core.annotation.Single
import org.litote.kmongo.and
import org.litote.kmongo.coroutine.CoroutineDatabase
import org.litote.kmongo.div
import org.litote.kmongo.eq
import org.litote.kmongo.ne

@Single
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
        return customersCollection.findOneById(customerId.toIdSecure("customerId"))
            ?: throw CustomerNotExists(customerId)
    }

    override suspend fun findCustomerByIdOrNull(customerId: String): CustomerEntity? {
        return customersCollection.findOneById(customerId.toIdSecure("customerId"))
    }

    override suspend fun updateCustomerData(customerId: String, data: CustomerDataEntity): CustomerEntity {
        val customer = customersCollection.findOneById(
            customerId.toIdSecure("customerId"),
        ) ?: throw CustomerNotExists(customerId = customerId)
        val newCustomer = customer.copy(data = data)
        customersCollection.save(newCustomer)
        return newCustomer
    }

    override suspend fun insertCustomer(data: CustomerDataEntity?, companyId: String): CustomerEntity {
        return CustomerEntity(data = data,
            companyReference = CompanyReferenceEntity(companyId)).also { customersCollection.insertOne(it) }
    }

    override suspend fun insertCustomers(count: Int, companyId: String): List<CustomerEntity> {
        return List(count) { CustomerEntity(companyReference = CompanyReferenceEntity(companyId)) }.also {
            customersCollection.insertMany(it)
        }
    }

    override suspend fun insertCard(customerId: String, companyId: String): CardEntity {
        if (!isCustomerExists(customerId))
            throw CustomerNotExists(customerId)
        return CardEntity(customerId = customerId,
            companyReference = CompanyReferenceEntity(companyId)).also { cardsCollection.insertOne(it) }
    }

    override suspend fun insertCards(
        customers: List<CustomerEntity>,
        companyId: String,
    ): Map<CardEntity, CustomerEntity> {
        val cards = customers.associateBy {
            CardEntity(
                customerId = it.id.toString(),
                companyReference = CompanyReferenceEntity(companyId)
            )
        }
        cardsCollection.insertMany(cards.keys.toList())
        return cards
    }

    override suspend fun findCardById(cardId: String): CardEntity {
        return cardsCollection.findOneById(cardId.toIdSecure("cardId")) ?: throw CardNotExistsWithId(cardId)
    }

    override suspend fun findCardByCustomerId(customerId: String): CardEntity {
        return cardsCollection.findOne(CardEntity::customerId eq customerId)
            ?: throw CardNotExistsForCustomer(customerId)
    }

    override suspend fun findCustomerByPhoneNumber(number: String, companyId: String): List<CustomerEntity> {
        return customersCollection.find(and(
            CustomerEntity::data / CustomerDataEntity::phone / PhoneNumberEntity::number eq number,
            CustomerEntity::companyReference eq CompanyReferenceEntity(companyId)
        )).toList()
    }

    override suspend fun findAllCustomers(offset: Int, page: Int, companyId: String): List<CustomerEntity> {
        return customersCollection.find(and(CustomerEntity::data ne null,
            CustomerEntity::companyReference eq CompanyReferenceEntity(companyId)
        )).ascendingSort(CustomerEntity::data / CustomerDataEntity::name)
            .paged(offset, page)
            .toList()
    }

}