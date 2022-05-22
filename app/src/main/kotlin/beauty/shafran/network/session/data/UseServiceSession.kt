package beauty.shafran.network.session.data

import beauty.shafran.network.companies.data.CompanyStationId
import beauty.shafran.network.employees.data.EmployeeId
import com.arkivanov.essenty.parcelable.Parcelable
import com.arkivanov.essenty.parcelable.Parcelize
import kotlinx.serialization.Serializable

@Serializable
@Parcelize
data class UseSessionRequest(
    val sessionId: ServiceSessionId,
    val employeeId: EmployeeId,
    val stationId: CompanyStationId,
    val note: String? = null,
) : Parcelable

@Serializable
@Parcelize
data class UseSessionResponse(
    val serviceSession: ServiceSession,
) : Parcelable

@Parcelize
@Serializable
data class DeactivateSessionRequest(
    val sessionId: ServiceSessionId,
    val data: DeactivateSessionRequestData,
) : Parcelable

@Serializable
@Parcelize
data class DeactivateSessionRequestData(
    val employeeId: EmployeeId,
    val note: String?,
    val reason: SessionManualDeactivationReason,
) : Parcelable

@Parcelize
@Serializable
data class DeactivateSessionResponse(
    val serviceSession: ServiceSession,
) : Parcelable