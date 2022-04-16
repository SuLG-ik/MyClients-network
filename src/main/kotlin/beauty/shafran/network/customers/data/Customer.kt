package beauty.shafran.network.customers.data

import beauty.shafran.network.Gender
import beauty.shafran.network.ZonedDateTimeSerializer
import beauty.shafran.network.phone.data.PhoneNumber
import com.arkivanov.essenty.parcelable.Parcelable
import com.arkivanov.essenty.parcelable.Parcelize
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.time.ZonedDateTime
import javax.validation.constraints.Size

@Serializable
sealed class Customer {

    abstract val id: String

    @Parcelize
    @Serializable
    @SerialName("inactivated")
    data class InactivatedCustomer(
        override val id: String,
    ) : Customer(), Parcelable


    @Parcelize
    @Serializable
    @SerialName("activated")
    data class ActivatedCustomer(
        override val id: String,
        val data: CustomerData,
    ) : Customer(), Parcelable

}


@Parcelize
@Serializable
data class CustomerData(
    @field:Size(min = 2)
    val name: String,
    val phone: PhoneNumber?,
    val remark: String,
    val gender: Gender,
    @Serializable(ZonedDateTimeSerializer::class)
    val activationDate: ZonedDateTime = ZonedDateTime.now(),
) : Parcelable

fun CustomerData.trim(): CustomerData {
    return copy(name = name.trim(), remark = remark.trim())
}