package beauty.shafran.network

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import java.time.Instant
import java.time.ZoneId
import java.time.ZonedDateTime

internal object ZonedDateTimeSerializer : KSerializer<ZonedDateTime> {
    override fun deserialize(decoder: Decoder): ZonedDateTime {
        return ZonedDateTime.ofInstant(Instant.ofEpochMilli(decoder.decodeLong()),
            ZoneId.systemDefault())
    }

    override val descriptor: SerialDescriptor
        get() = PrimitiveSerialDescriptor(ZonedDateTime::class.java.name, PrimitiveKind.LONG)

    override fun serialize(encoder: Encoder, value: ZonedDateTime) {
        encoder.encodeLong(value.toInstant().toEpochMilli())
    }
}