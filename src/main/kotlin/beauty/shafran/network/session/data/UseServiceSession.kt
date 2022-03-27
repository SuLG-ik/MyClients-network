package beauty.shafran.network.session.data

import com.arkivanov.essenty.parcelable.Parcelable
import com.arkivanov.essenty.parcelable.Parcelize
import kotlinx.serialization.Serializable

@Serializable
@Parcelize
data class UseSessionRequest(
    val sessionId: String,
    val employeeId: String,
    val note: String? = null,
): Parcelable

@Serializable
@Parcelize
data class UseSessionResponse(
    val session: Session,
): Parcelable