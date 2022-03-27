package beauty.shafran.network.phone.converters

import beauty.shafran.network.PhoneNumber
import beauty.shafran.network.phone.entity.PhoneNumberEntity

class PhoneNumberConverterImpl : PhoneNumberConverter {
    override fun PhoneNumber.toEntity(): PhoneNumberEntity {
        return PhoneNumberEntity(
            countryCode = countryCode,
            number = number,
        )
    }

    override fun PhoneNumberEntity.toData(): PhoneNumber {
        return PhoneNumber(
            countryCode = countryCode,
            number = number,
        )
    }
}