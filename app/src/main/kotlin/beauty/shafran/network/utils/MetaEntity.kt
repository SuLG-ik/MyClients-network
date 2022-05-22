package beauty.shafran.network.utils

import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlinx.serialization.Serializable

@Serializable
class MetaEntity(
    val creationDate: LocalDateTime = Clock.System.now().toLocalDateTime(TimeZone.UTC),
)