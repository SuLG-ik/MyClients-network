package beauty.shafran.network.phone.converters

import beauty.shafran.network.phone.data.PhoneNumber
import beauty.shafran.network.phone.entity.PhoneNumberEntity


interface PhoneNumberConverter {

    fun PhoneNumber.toEntity(): PhoneNumberEntity?

    fun PhoneNumberEntity.toData(): PhoneNumber

}