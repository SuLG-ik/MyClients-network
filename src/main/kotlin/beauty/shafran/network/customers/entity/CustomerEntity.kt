package beauty.shafran.network.customers.entity

import beauty.shafran.network.Gender
import beauty.shafran.network.phone.entity.PhoneNumberEntity
import kotlinx.serialization.Contextual
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.litote.kmongo.Id
import org.litote.kmongo.newId

@Serializable
data class CustomerEntity(
    val data: CustomerDataEntity? = null,
    @Contextual
    @SerialName("_id")
    val id: Id<CustomerEntity> = newId(),
)

@Serializable
data class CustomerDataEntity(
    val name: String,
    val phone: PhoneNumberEntity?,
    val gender: Gender,
    val remark: String,
)

