package beauty.shafran.network.customers.converters

import beauty.shafran.network.customers.data.Customer
import beauty.shafran.network.customers.data.CustomerData
import beauty.shafran.network.customers.data.EditableCustomerData
import beauty.shafran.network.customers.entity.CustomerDataEntity
import beauty.shafran.network.customers.entity.CustomerEntity
import beauty.shafran.network.phone.converters.PhoneNumberConverter
import org.koin.core.annotation.Single
import java.time.ZoneId
import java.time.ZonedDateTime

@Single
class MongoCustomersConverter(private val phoneNumberConverter: PhoneNumberConverter) : CustomersConverter {


    override fun CustomerEntity.toData(): Customer {
        return if (data == null) {
            Customer.InactivatedCustomer(id = id.toString())
        } else {
            Customer.ActivatedCustomer(
                id = id.toString(),
                data = data.toData(),
            )
        }
    }

    override fun CustomerDataEntity.toData(): CustomerData {
        return CustomerData(
            name = name,
            phone = with(phoneNumberConverter) { phone?.toData() },
            remark = remark,
            gender = gender,
            activationDate = ZonedDateTime.ofInstant(activationDate.toInstant(), ZoneId.systemDefault())
        )
    }

    override fun EditableCustomerData.toNewEntity(): CustomerDataEntity {
        return CustomerDataEntity(
            name = name,
            phone = with(phoneNumberConverter) { phone?.toEntity() },
            gender = gender,
            remark = remark,
        )
    }

}