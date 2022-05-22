package beauty.shafran.network.customers.data

import beauty.shafran.network.gender.Gender
import beauty.shafran.network.phone.data.PhoneNumber
import beauty.shafran.network.utils.MetaData
import com.arkivanov.essenty.parcelable.Parcelable
import com.arkivanov.essenty.parcelable.Parcelize
import kotlinx.serialization.Serializable

@JvmInline
@Serializable
value class CustomerId(val id: Long)


@Serializable
data class Customer(
    val id: CustomerId,
    val data: CustomerData,
    val meta: MetaData,
)


@Parcelize
@Serializable
data class CustomerData(
    val name: String,
    val phone: PhoneNumber?,
    val description: String,
    val gender: Gender,
) : Parcelable
