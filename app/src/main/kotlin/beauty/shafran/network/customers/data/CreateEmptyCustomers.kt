package beauty.shafran.network.customers.data

import com.arkivanov.essenty.parcelable.Parcelable
import com.arkivanov.essenty.parcelable.Parcelize
import kotlinx.serialization.Serializable

@Parcelize
@Serializable
data class CreateCustomersRequest(
    val data: EditableCustomerData,
    val storageId: CustomersStorageId,
) : Parcelable


@Parcelize
@Serializable
data class CreateCustomerResponse(
    val customer: Customer,
) : Parcelable