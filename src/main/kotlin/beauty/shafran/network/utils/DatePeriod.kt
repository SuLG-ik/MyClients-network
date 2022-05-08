@file:UseSerializers(LocalDateSerializer::class)

package beauty.shafran.network.utils

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.UseSerializers
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import java.time.LocalDate
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.ZonedDateTime
import java.util.*

@Serializable
data class DatePeriod(
    val from: LocalDate,
    val to: LocalDate,
)

fun Date.toLocalDate(): LocalDate {
    return LocalDate.ofInstant(toInstant(), ZoneId.systemDefault())
}


fun LocalDate.toStartOfDate(): Date {
    return Date.from(atStartOfDay().toInstant(ZoneOffset.UTC))
}

fun Date.toZonedDateTime(zoneId: ZoneId = ZoneId.systemDefault()): ZonedDateTime {
    return ZonedDateTime.ofInstant(toInstant(), zoneId)
}

object LocalDateSerializer : KSerializer<LocalDate> {

    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("LocalDate", PrimitiveKind.LONG)

    override fun serialize(encoder: Encoder, value: LocalDate) {
        encoder.encodeLong(value.toEpochDay())
    }

    override fun deserialize(decoder: Decoder): LocalDate {
        val days = decoder.decodeLong()
        return LocalDate.ofEpochDay(days)
    }

}
