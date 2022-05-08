package beauty.shafran.network.phone.data

import com.arkivanov.essenty.parcelable.Parcelize
import kotlinx.serialization.Serializable

@Serializable
@Parcelize
data class PhoneNumber(
    val number: String,
)