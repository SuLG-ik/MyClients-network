package beauty.shafran.network.customers.data

import com.arkivanov.essenty.parcelable.Parcelize
import kotlinx.serialization.Serializable

@Parcelize
@Serializable
class RestoreCustomerRequest(
    val token: String,
)