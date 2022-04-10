@file:UseSerializers(ZonedDateTimeSerializer::class)

package beauty.shafran.network.session.data

import beauty.shafran.network.ZonedDateTimeSerializer
import beauty.shafran.network.employees.data.Employee
import beauty.shafran.network.services.data.ConfiguredService
import com.arkivanov.essenty.parcelable.Parcelable
import com.arkivanov.essenty.parcelable.Parcelize
import kotlinx.serialization.Serializable
import kotlinx.serialization.UseSerializers
import java.time.ZonedDateTime

@Serializable
@Parcelize
data class Session(
    val id: String,
    val activation: SessionActivation,
    val usages: List<SessionUsage>,
    val manualDeactivation: SessionManualDeactivation?,
) : Parcelable

@Serializable
@Parcelize
data class SessionActivation(
    val employee: Employee,
    val service: ConfiguredService,
    val customerId: String,
    val note: String?,
) : Parcelable

@Serializable
@Parcelize
data class SessionUsage(
    val id: String,
    val data: SessionUsageData,
) : Parcelable

@Serializable
@Parcelize
data class SessionUsageData(
    val employee: Employee,
    val note: String?,
    val date: ZonedDateTime,
) : Parcelable

@Serializable
@Parcelize
data class SessionManualDeactivation(
    val id: String,
    val data: SessionManualDeactivationData,
) : Parcelable

@Serializable
@Parcelize
data class SessionManualDeactivationData(
    val note: String?,
    val reason: SessionManualDeactivationReason,
    val employee: Employee,
    val date: ZonedDateTime,
) : Parcelable


enum class SessionManualDeactivationReason {
    UNKNOWN, MISS_CLICK, CANCEL_BY_CUSTOMER, CANCEL_BY_BUSINESS,
}