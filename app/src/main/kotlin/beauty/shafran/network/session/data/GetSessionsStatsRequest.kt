package beauty.shafran.network.session.data

import beauty.shafran.network.services.data.Service
import beauty.shafran.network.session.entity.ServiceSessionStorageId
import beauty.shafran.network.utils.DatePeriod
import com.arkivanov.essenty.parcelable.Parcelable
import com.arkivanov.essenty.parcelable.Parcelize
import kotlinx.datetime.LocalDate
import kotlinx.serialization.Serializable

@Serializable
@Parcelize
data class GetSessionsStatsRequest(
    val storageId: ServiceSessionStorageId,
    val period: DatePeriod,
) : Parcelable

@Serializable
@Parcelize
data class GetSessionsStatsResponse(
    val stats: SessionStats,
    val period: DatePeriod,
) : Parcelable

@Parcelize
@Serializable
data class SessionStats(
    val activatedSessionsCount: Int,
    val usedSessionsCount: Int,
    val popularService: MostPopularService,
    val usages: List<DayToCountUsageStat>,
) : Parcelable

@Serializable
@Parcelize
data class DayToCountUsageStat(
    val day: LocalDate,
    val count: Int,
) : Parcelable

@Parcelize
@Serializable
data class MostPopularService(
    val count: Int,
    val configuredService: Service,
) : Parcelable