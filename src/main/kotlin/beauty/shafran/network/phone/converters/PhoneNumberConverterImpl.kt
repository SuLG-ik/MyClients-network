package beauty.shafran.network.phone.converters

import beauty.shafran.network.phone.data.PhoneNumber
import beauty.shafran.network.phone.entity.PhoneNumberEntity
import org.springframework.stereotype.Service

@Service
class PhoneNumberConverterImpl : PhoneNumberConverter {
    override fun PhoneNumber.toEntity(): PhoneNumberEntity? {
        return if (isValid) PhoneNumberEntity(
            number = number
        ) else null
    }


    private val PhoneNumber.isValid: Boolean
        get() {
            return number.length == 11
        }

    override fun PhoneNumberEntity.toData(): PhoneNumber {
        return PhoneNumber(
            number = number,
        )
    }
}