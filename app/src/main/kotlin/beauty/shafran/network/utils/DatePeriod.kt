package beauty.shafran.network.utils

import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable

@Serializable
data class DatePeriod(
    val from: LocalDate,
    val to: LocalDate,
)

@Serializable
data class MetaData(
    val creationDate: LocalDateTime,
)

fun LocalDateTime.toMetaData(): MetaData {
    return MetaData(this)
}