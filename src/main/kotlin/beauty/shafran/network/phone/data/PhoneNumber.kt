package beauty.shafran.network.phone.data

import com.arkivanov.essenty.parcelable.Parcelize
import jakarta.validation.constraints.Size
import kotlinx.serialization.Serializable

@Serializable
@Parcelize
data class PhoneNumber(
    @field:Size(min = 11, max = 11)
    val number: String,
)