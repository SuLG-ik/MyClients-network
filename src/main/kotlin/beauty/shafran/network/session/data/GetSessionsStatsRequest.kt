package beauty.shafran.network.session.data

import beauty.shafran.network.services.data.Service
import beauty.shafran.network.utils.DatePeriod
import beauty.shafran.network.utils.LocalDateSerializer
import kotlinx.serialization.Serializable
import java.time.LocalDate

@Serializable
data class GetSessionsStatsRequest(
    val period: DatePeriod = DatePeriod(from = LocalDate.now().minusMonths(1), to = LocalDate.now()),
)

@Serializable
data class GetSessionsStatsResponse(
    val stats: SessionStats,
    val period: DatePeriod,
)

@Serializable
data class SessionStats(
    val activatedSessionsCount: Int,
    val usedSessionsCount: Int,
    val popularService: MostPopularService,
    val usages: List<DayToCountUsageStat>,
)

@Serializable
data class DayToCountUsageStat(
    @Serializable(LocalDateSerializer::class)
    val day: LocalDate,
    val count: Int,
)

@Serializable
data class MostPopularService(
    val count: Int,
    val configuredService: Service,
)