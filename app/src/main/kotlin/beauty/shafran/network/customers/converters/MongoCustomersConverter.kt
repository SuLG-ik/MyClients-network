package beauty.shafran.network.customers.converters

import beauty.shafran.network.customers.data.Customer
import beauty.shafran.network.customers.data.CustomerData
import beauty.shafran.network.customers.data.CustomerId
import beauty.shafran.network.customers.entity.CustomerDataEntity
import beauty.shafran.network.customers.entity.CustomerEntity
import beauty.shafran.network.phone.converters.PhoneNumberConverter
import beauty.shafran.network.utils.toMetaData


class MongoCustomersConverter(private val phoneNumberConverter: PhoneNumberConverter) : CustomersConverter {
    override fun buildCustomer(entity: CustomerEntity, data: CustomerDataEntity): Customer {
        return Customer(
            id = CustomerId(entity.id),
            data = CustomerData(
                name = data.name,
                description = data.description,
                gender = data.gender,
                phone = null,
            ),
            meta = data.creationDate.toMetaData()
        )
    }


}