package beauty.shafran.network.customers.converters

import beauty.shafran.network.customers.data.Customer
import beauty.shafran.network.customers.data.CustomerData
import beauty.shafran.network.customers.data.EditableCustomerData
import beauty.shafran.network.customers.entity.CustomerDataEntity
import beauty.shafran.network.customers.entity.CustomerEntity

interface CustomersConverter {
    fun CustomerEntity.toData(): Customer
    fun CustomerDataEntity.toData(): CustomerData
    fun EditableCustomerData.toNewEntity(): CustomerDataEntity
}