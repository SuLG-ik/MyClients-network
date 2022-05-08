package beauty.shafran.network.customers.repository

import beauty.shafran.CustomerNotExists
import beauty.shafran.network.customers.entity.*
import beauty.shafran.network.utils.selectLatest
import org.jetbrains.exposed.sql.insertAndGetId
import org.jetbrains.exposed.sql.select

class PostgresCustomersRepository : CustomersRepository {

    override suspend fun throwIfCustomerNotExists(customerId: CustomerId) {
        if (!isCustomerExists(customerId))
            throw CustomerNotExists(customerId)
    }

    override suspend fun isCustomerExists(customerId: CustomerId): Boolean {
        return CustomersTable.select { CustomersTable.id eq customerId.id }.count() > 0
    }

    override suspend fun findCustomerById(customerId: CustomerId): CustomerEntity {
        throwIfCustomerNotExists(customerId)
        val data =
            CustomersDataTable.selectLatest { CustomersDataTable.customerId eq customerId.id }!!.toCustomerDataEntity()
        return CustomerEntity(
            id = customerId,
            data = data,
        )
    }

    override suspend fun findCustomerByIdOrNull(customerId: CustomerId): CustomerEntity? {
        if (!isCustomerExists(customerId))
            return null
        val data =
            CustomersDataTable.selectLatest { CustomersDataTable.customerId eq customerId.id }!!.toCustomerDataEntity()
        return CustomerEntity(
            id = customerId,
            data = data,
        )
    }

    override suspend fun updateCustomerData(customerId: CustomerId, data: CustomerDataEntity): CustomerEntity {
        CustomersDataTable.insertAndGetId {
            it[this.customerId] = customerId.id
            it[this.name] = data.name
            it[this.gender] = data.gender
            it[this.description] = data.description
        }
        return CustomerEntity(
            id = customerId,
            data = data,
        )
    }

    override suspend fun insertCustomer(data: CustomerDataEntity): CustomerEntity {
        TODO("Not yet implemented")
    }

    override suspend fun insertCustomers(count: Int): List<CustomerEntity> {
        TODO("Not yet implemented")
    }

    override suspend fun insertCard(customerId: CustomerId, companyId: String): CardEntity {
        TODO("Not yet implemented")
    }

    override suspend fun insertCards(
        customers: List<CustomerEntity>,
        companyId: String,
    ): Map<CardEntity, CustomerEntity> {
        TODO("Not yet implemented")
    }

    override suspend fun findCardById(cardId: String): CardEntity {
        TODO("Not yet implemented")
    }

    override suspend fun findCardByCustomerId(customerId: CustomerId): CardEntity {
        TODO("Not yet implemented")
    }

    override suspend fun findCustomerByPhoneNumber(number: String, companyId: String): List<CustomerEntity> {
        TODO("Not yet implemented")
    }

    override suspend fun findAllCustomers(offset: Int, page: Int, companyId: String): List<CustomerEntity> {
        TODO("Not yet implemented")
    }
}