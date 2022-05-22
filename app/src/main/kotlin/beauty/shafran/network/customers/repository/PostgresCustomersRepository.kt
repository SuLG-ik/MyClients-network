package beauty.shafran.network.customers.repository

import beauty.shafran.CustomerNotExists
import beauty.shafran.network.customers.data.CustomerId
import beauty.shafran.network.customers.data.CustomersStorageId
import beauty.shafran.network.customers.data.EditableCustomerData
import beauty.shafran.network.customers.entity.*
import beauty.shafran.network.utils.PagedData
import beauty.shafran.network.utils.TransactionalScope
import beauty.shafran.network.utils.isRowExists
import kotlinx.coroutines.awaitAll
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.jetbrains.exposed.sql.select
import org.koin.core.annotation.Single

@Single
class PostgresCustomersRepository : CustomersRepository {

    context (TransactionalScope) override suspend fun throwIfCustomerNotExists(customerId: CustomerId) {
        if (!isCustomerExists(customerId)) throw CustomerNotExists(customerId.toString())
    }

    context (TransactionalScope) override suspend fun isCustomerExists(customerId: CustomerId): Boolean {
        return CustomerTable.isRowExists { CustomerTable.id eq customerId.id }
    }

    context (TransactionalScope)  override suspend fun findCustomerById(customerId: CustomerId): CustomerEntity {
        return CustomerTable.findById(customerId.id) ?: throw CustomerNotExists(customerId.toString())
    }

    context (TransactionalScope) override suspend fun findCustomerDataById(customerId: CustomerId): CustomerDataEntity {
        return CustomerDataTable.select { CustomerDataTable.customerId eq customerId.id }.firstOrNull()
            ?.toCustomerDataEntity() ?: throw CustomerNotExists(customerId.toString())
    }

    context (TransactionalScope) override suspend fun findCustomerByIdOrNull(customerId: CustomerId): CustomerEntity? {
        return CustomerTable.findById(customerId.id)
    }

    context (TransactionalScope) override suspend fun updateCustomerData(
        customerId: CustomerId,
        data: EditableCustomerData,
    ) {
        CustomerDataTable.insertEntity(
            customerId = customerId.id,
            name = data.name,
            gender = data.gender,
            description = data.description ?: "",
        )
    }

    context (TransactionalScope) override suspend fun insertCustomer(
        data: EditableCustomerData,
        storageId: CustomersStorageId,
    ): CustomerEntity {
        val currentTime = Clock.System.now().toLocalDateTime(TimeZone.UTC)
        val customerId = CustomerTable.insertEntityAndGetId(creationDate = currentTime)
        listOf(
            transactionAsync {
                CustomerDataTable.insertEntity(
                    customerId = customerId,
                    name = data.name,
                    gender = data.gender,
                    description = data.description ?: "",
                    creationDate = currentTime,
                )
            },
            transactionAsync {
                CustomerToStorageTable.insertEntity(
                    storageId = storageId.id,
                    customerId = customerId,
                    creationDate = currentTime
                )
            }).awaitAll()
        return CustomerEntity(
            id = customerId,
            creationDate = currentTime,
        )
    }


    context (TransactionalScope) override suspend fun findCustomerByPhoneNumber(
        number: String,
        storageId: CustomersStorageId,
    ): List<CustomerEntity> {
        TODO("Not yet implemented")
    }

    context (TransactionalScope) override suspend fun findAllCustomers(
        paged: PagedData,
        storageId: CustomersStorageId,
    ): List<CustomerEntity> {
        val customers = CustomerToStorageTable.select { CustomerToStorageTable.storageId eq storageId.id }
            .map { it.toCustomerToStorageEntity().customerId }
        return CustomerTable.select { CustomerTable.id inList customers }.map { it.toCustomerEntity() }
    }

    context(TransactionalScope) override suspend fun findAllCustomersData(customers: List<CustomerId>): Map<CustomerId, CustomerDataEntity> {
        return CustomerDataTable.select { CustomerDataTable.customerId inList customers.map { it.id } }
            .associate {
                val entity = it.toCustomerDataEntity()
                CustomerId(entity.customerId) to entity
            }
    }


}