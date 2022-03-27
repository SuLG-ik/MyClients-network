package beauty.shafran.network.customers.data

import com.arkivanov.essenty.parcelable.Parcelable
import com.arkivanov.essenty.parcelable.Parcelize
import kotlinx.serialization.Serializable

@Parcelize
@Serializable
data class CreateEmptyCustomersRequest(
    val count: Int,
) : Parcelable

@Parcelize
@Serializable
data class CreateEmptyCustomersResponse(
    val cards: Map<String, Customer.InactivatedCustomer>,
) : Parcelable


@Parcelize
@Serializable
data class CreateCustomersRequest(
    val data: CustomerData,
) : Parcelable


@Parcelize
@Serializable
data class CreateCustomersResponse(
    val token: String,
    val customer: Customer.ActivatedCustomer,
) : Parcelable