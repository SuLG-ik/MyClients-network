package beauty.shafran.network.customers.data

import beauty.shafran.network.companies.data.CompanyId
import com.arkivanov.essenty.parcelable.Parcelable
import com.arkivanov.essenty.parcelable.Parcelize
import kotlinx.serialization.Serializable

@Parcelize
@Serializable
data class CreateEmptyCustomersRequest(
    val count: Int,
    val companyId: String,
) : Parcelable

@Parcelize
@Serializable
data class CreateEmptyCustomersResponse(
    val cards: Map<String, Customer.InactivatedCustomer>,
) : Parcelable


@Parcelize
@Serializable
data class CreateCustomersRequest(
    val data: EditableCustomerData,
    val companyId: CompanyId,
) : Parcelable


@Parcelize
@Serializable
data class CreateCustomerResponse(
    val token: String,
    val customer: Customer.ActivatedCustomer,
) : Parcelable