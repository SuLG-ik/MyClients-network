package beauty.shafran.network.phone.data

import com.arkivanov.essenty.parcelable.Parcelize
import kotlinx.serialization.Serializable
import javax.validation.constraints.Size

@Serializable
@Parcelize
data class PhoneNumber(
    @field:Size(min = 11, max = 11)
    val number: String,
)