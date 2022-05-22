package beauty.shafran.network.customers.repository

import beauty.shafran.network.customers.data.CustomerId
import beauty.shafran.network.customers.data.CustomersStorageId
import beauty.shafran.network.customers.data.EditableCustomerData
import beauty.shafran.network.customers.entity.CustomerDataEntity
import beauty.shafran.network.customers.entity.CustomerEntity
import beauty.shafran.network.utils.PagedData
import beauty.shafran.network.utils.TransactionalScope

interface CustomersRepository {

    context (TransactionalScope) suspend fun findCustomerById(customerId: CustomerId): CustomerEntity

    context (TransactionalScope) suspend fun findCustomerDataById(customerId: CustomerId): CustomerDataEntity

    context (TransactionalScope) suspend fun findCustomerByIdOrNull(customerId: CustomerId): CustomerEntity?

    context (TransactionalScope) suspend fun updateCustomerData(customerId: CustomerId, data: EditableCustomerData)

    context (TransactionalScope) suspend fun insertCustomer(
        data: EditableCustomerData,
        storageId: CustomersStorageId,
    ): CustomerEntity

    context (TransactionalScope) suspend fun findCustomerByPhoneNumber(
        number: String,
        storageId: CustomersStorageId,
    ): List<CustomerEntity>

    context (TransactionalScope) suspend fun findAllCustomers(
        paged: PagedData,
        storageId: CustomersStorageId,
    ): List<CustomerEntity>

    context (TransactionalScope) suspend fun findAllCustomersData(
        customers: List<CustomerId>,
    ): Map<CustomerId, CustomerDataEntity>

    context (TransactionalScope) suspend fun isCustomerExists(customerId: CustomerId): Boolean
    context (TransactionalScope) suspend fun throwIfCustomerNotExists(customerId: CustomerId)

}