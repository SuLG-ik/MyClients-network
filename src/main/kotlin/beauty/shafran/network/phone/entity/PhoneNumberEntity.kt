package beauty.shafran.network.phone.entity

import kotlinx.serialization.Serializable

@Serializable
data class PhoneNumberEntity(
    val countryCode: String,
    val number: String,
)