package beauty.shafran.network.customers.data

import beauty.shafran.network.phone.data.PhoneNumber
import com.arkivanov.essenty.parcelable.Parcelable
import com.arkivanov.essenty.parcelable.Parcelize
import kotlinx.serialization.Serializable

@Parcelize
@Serializable
data class SearchCustomerByPhoneRequest(
    val phoneNumber: PhoneNumber,
) : Parcelable

@Parcelize
@Serializable
data class SearchCustomerByPhoneResponse(
    val searchResult: List<FoundCustomerItem>,
) : Parcelable
