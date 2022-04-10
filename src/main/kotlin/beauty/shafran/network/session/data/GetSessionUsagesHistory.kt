package beauty.shafran.network.session.data

import beauty.shafran.network.customers.data.Customer
import beauty.shafran.network.services.data.ConfiguredService
import com.arkivanov.essenty.parcelable.Parcelable
import com.arkivanov.essenty.parcelable.Parcelize
import kotlinx.serialization.Serializable
import javax.validation.constraints.Min


@Serializable
@Parcelize
data class GetSessionUsagesHistoryRequest(
    @field:Min(1)
    val offset: Int = 30,
    @field:Min(0)
    val page: Int = 0,
) : Parcelable


@Serializable
@Parcelize
data class GetSessionUsagesHistoryResponse(
    val usages: List<SessionUsageHistoryItem>,
    val offset: Int,
    val page: Int,
) : Parcelable

@Parcelize
@Serializable
data class SessionUsageHistoryItem(
    val service: ConfiguredService,
    val usage: SessionUsage,
    val customer: Customer.ActivatedCustomer,
) : Parcelable

