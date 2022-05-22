package beauty.shafran.network.session.data

import beauty.shafran.network.employees.data.Employee
import beauty.shafran.network.services.data.ConfiguredService
import com.arkivanov.essenty.parcelable.Parcelable
import com.arkivanov.essenty.parcelable.Parcelize
import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable

@Serializable
@Parcelize
data class ServiceSession(
    val id: ServiceSessionId,
    val activation: ServiceSessionActivation,
    val usages: List<ServiceSessionUsage>,
    val date: LocalDateTime,
    val manualDeactivation: ServiceSessionManualDeactivation?,
) : Parcelable

@Serializable
@Parcelize
data class ServiceSessionActivation(
    val employee: Employee,
    val service: ConfiguredService,
    val customerId: String,
    val note: String?,
) : Parcelable

@Serializable
@Parcelize
data class ServiceSessionUsage(
    val id: String,
    val data: ServiceSessionUsageData,
) : Parcelable

@Serializable
@Parcelize
data class ServiceSessionUsageData(
    val employee: Employee,
    val note: String?,
    val date: LocalDateTime,
) : Parcelable

@Serializable
@Parcelize
data class ServiceSessionManualDeactivation(
    val id: String,
    val data: SessionManualDeactivationData,
) : Parcelable

@Serializable
@Parcelize
data class SessionManualDeactivationData(
    val note: String?,
    val reason: SessionManualDeactivationReason,
    val employee: Employee,
    val date: LocalDateTime,
) : Parcelable

@JvmInline
@Serializable
value class ServiceSessionId(
    val id: Long,
)

@JvmInline
@Serializable
value class ServiceSessionUsageId(
    val id: Long,
)

@JvmInline
@Serializable
value class ServiceSessionDeactivationId(
    val id: Long,
)


enum class SessionManualDeactivationReason {
    UNKNOWN, MISS_CLICK, MONEY_BACK;

    companion object
}

val SessionManualDeactivationReason.Companion.typeName get() = "service_session_manual_deactivation_reason"