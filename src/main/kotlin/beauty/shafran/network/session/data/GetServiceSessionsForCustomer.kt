package beauty.shafran.network.session.data

import beauty.shafran.network.validation.ObjectIdParameter
import com.arkivanov.essenty.parcelable.Parcelable
import com.arkivanov.essenty.parcelable.Parcelize
import kotlinx.serialization.Serializable

@Serializable
@Parcelize
data class GetSessionsForCustomerRequest(
    @field:ObjectIdParameter
    val customerId: String,
): Parcelable

@Serializable
@Parcelize
data class GetSessionsForCustomerResponse(
    val sessions: List<Session>,
):Parcelable