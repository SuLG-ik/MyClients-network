package beauty.shafran.network.customers.data

import beauty.shafran.network.session.data.Session
import com.arkivanov.essenty.parcelable.Parcelable
import com.arkivanov.essenty.parcelable.Parcelize
import kotlinx.serialization.Serializable

@Parcelize
@Serializable
data class FoundCustomerItem(
    val customer: Customer.ActivatedCustomer,
    val lastUsedSession: Session?,
) : Parcelable