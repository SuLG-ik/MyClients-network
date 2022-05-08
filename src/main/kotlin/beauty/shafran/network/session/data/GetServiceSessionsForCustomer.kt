package beauty.shafran.network.session.data

import com.arkivanov.essenty.parcelable.Parcelable
import com.arkivanov.essenty.parcelable.Parcelize
import kotlinx.serialization.Serializable

@Serializable
@Parcelize
data class GetSessionsForCustomerRequest(
    val customerId: String,
): Parcelable

@Serializable
@Parcelize
data class GetSessionsForCustomerResponse(
    val sessions: List<Session>,
):Parcelable