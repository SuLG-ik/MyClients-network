package beauty.shafran.network.customers.data

import com.arkivanov.essenty.parcelable.Parcelable
import com.arkivanov.essenty.parcelable.Parcelize
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import beauty.shafran.network.Gender
import beauty.shafran.network.phone.data.PhoneNumber

@Serializable
sealed class Customer {

    abstract val id: String

    @Parcelize
    @Serializable
    @SerialName("inactivated")
    data class InactivatedCustomer(
        override val id: String,
    ): Customer(), Parcelable


    @Parcelize
    @Serializable
    @SerialName("activated")
    data class ActivatedCustomer(
        override val id: String,
        val data: CustomerData,
    ): Customer(), Parcelable

}


@Parcelize
@Serializable
data class CustomerData(
    val name: String,
    val phone: PhoneNumber?,
    val remark: String,
    val gender: Gender,
): Parcelable