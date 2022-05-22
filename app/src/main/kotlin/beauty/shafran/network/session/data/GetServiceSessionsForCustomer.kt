package beauty.shafran.network.session.data

import beauty.shafran.network.customers.data.CustomerId
import beauty.shafran.network.session.entity.ServiceSessionStorageId
import com.arkivanov.essenty.parcelable.Parcelable
import com.arkivanov.essenty.parcelable.Parcelize
import kotlinx.serialization.Serializable

@Serializable
@Parcelize
data class GetSessionsForCustomerRequest(
    val customerId: CustomerId,
    val storageId: ServiceSessionStorageId,
) : Parcelable

@Serializable
@Parcelize
data class GetSessionsForCustomerResponse(
    val serviceSessions: List<ServiceSession>,
) : Parcelable