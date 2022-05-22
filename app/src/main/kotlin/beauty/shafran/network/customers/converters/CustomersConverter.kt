package beauty.shafran.network.customers.converters

import beauty.shafran.network.customers.data.Customer
import beauty.shafran.network.customers.entity.CustomerDataEntity
import beauty.shafran.network.customers.entity.CustomerEntity

interface CustomersConverter {

    fun buildCustomer(entity: CustomerEntity, data: CustomerDataEntity): Customer

}