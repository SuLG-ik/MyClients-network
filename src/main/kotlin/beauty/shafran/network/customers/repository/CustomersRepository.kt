package beauty.shafran.network.customers.repository

import beauty.shafran.network.customers.entity.CardEntity
import beauty.shafran.network.customers.entity.CustomerDataEntity
import beauty.shafran.network.customers.entity.CustomerEntity
import beauty.shafran.network.customers.entity.CustomerId

interface CustomersRepository {

    suspend fun findCustomerById(customerId: CustomerId): CustomerEntity
    suspend fun findCustomerByIdOrNull(customerId: CustomerId): CustomerEntity?
    suspend fun updateCustomerData(customerId: CustomerId, data: CustomerDataEntity): CustomerEntity
    suspend fun insertCustomer(data: CustomerDataEntity?, companyId: String): CustomerEntity
    suspend fun insertCustomers(count: Int, companyId: String): List<CustomerEntity>
    suspend fun insertCard(customerId: CustomerId, companyId: String): CardEntity
    suspend fun insertCards(customers: List<CustomerEntity>, companyId: String): Map<CardEntity, CustomerEntity>
    suspend fun findCardById(cardId: String): CardEntity
    suspend fun findCardByCustomerId(customerId: CustomerId): CardEntity
    suspend fun findCustomerByPhoneNumber(number: String, companyId: String): List<CustomerEntity>
    suspend fun findAllCustomers(offset: Int, page: Int, companyId: String): List<CustomerEntity>


    suspend fun isCustomerExists(customerId: CustomerId): Boolean
    suspend fun throwIfCustomerNotExists(customerId: CustomerId)
}