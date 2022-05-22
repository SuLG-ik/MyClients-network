package beauty.shafran.network.session.data

import beauty.shafran.network.customers.data.Customer
import beauty.shafran.network.services.data.ConfiguredService
import beauty.shafran.network.session.entity.ServiceSessionStorageId
import beauty.shafran.network.utils.PagedData
import com.arkivanov.essenty.parcelable.Parcelable
import com.arkivanov.essenty.parcelable.Parcelize
import kotlinx.serialization.Serializable


@Serializable
@Parcelize
data class GetSessionUsagesHistoryRequest(
    val paged: PagedData,
    val storageId: ServiceSessionStorageId,
) : Parcelable


@Serializable
@Parcelize
data class GetSessionUsagesHistoryResponse(
    val usages: List<SessionUsageHistoryItem>,
    val paged: PagedData,
) : Parcelable

@Parcelize
@Serializable
data class SessionUsageHistoryItem(
    val service: ConfiguredService,
    val usage: ServiceSessionUsage,
    val customer: Customer,
) : Parcelable

