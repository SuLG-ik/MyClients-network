package beauty.shafran.network.customers.repository

import beauty.shafran.network.customers.entity.CardEntity
import beauty.shafran.network.customers.entity.CustomerDataEntity
import beauty.shafran.network.customers.entity.CustomerEntity

interface CustomersRepository {

    suspend fun throwIfCustomerNotExists(customerId: String)
    suspend fun isCustomerExists(customerId: String): Boolean
    suspend fun findCustomerById(customerId: String): CustomerEntity
    suspend fun updateCustomerData(customerId: String, data: CustomerDataEntity): CustomerEntity
    suspend fun insertCustomer(data: CustomerDataEntity?): CustomerEntity
    suspend fun insertCustomers(count: Int): List<CustomerEntity>
    suspend fun insertCard(customerId: String): CardEntity
    suspend fun insertCard(customerId: String, cardId: String): CardEntity
    suspend fun insertCards(customers: List<CustomerEntity>): Map<CardEntity, CustomerEntity>
    suspend fun findCardById(cardId: String): CardEntity
    suspend fun findCardByCustomerId(customerId: String): CardEntity
    suspend fun findCustomerByPhoneNumber(number: String): List<CustomerEntity>
    suspend fun findAllCustomers(offset: Int, page: Int): List<CustomerEntity>


}