package beauty.shafran.network.utils

import org.bson.types.ObjectId
import org.litote.kmongo.Id
import java.time.ZoneId
import java.time.ZonedDateTime

fun ObjectId.getZonedDateTime(zoneId: ZoneId = ZoneId.systemDefault()): ZonedDateTime {
    return ZonedDateTime.ofInstant(date.toInstant(), zoneId)
}


fun Id<*>.getZonedDateTime(zoneId: ZoneId = ZoneId.systemDefault()): ZonedDateTime {
    return ObjectId(toString()).getZonedDateTime(zoneId)
}