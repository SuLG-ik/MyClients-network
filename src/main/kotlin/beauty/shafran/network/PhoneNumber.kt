package beauty.shafran.network

import com.arkivanov.essenty.parcelable.Parcelable
import com.arkivanov.essenty.parcelable.Parcelize
import kotlinx.serialization.Serializable

@Serializable
@Parcelize
data class PhoneNumber(
    val countryCode: String,
    val number: String,
): Parcelable {

    override fun toString(): String {
        return "+$countryCode$number"
    }

}