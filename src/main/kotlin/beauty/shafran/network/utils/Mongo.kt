package beauty.shafran.network.utils

import beauty.shafran.IllegalId
import com.mongodb.client.model.CountOptions
import org.bson.conversions.Bson
import org.litote.kmongo.EMPTY_BSON
import org.litote.kmongo.Id
import org.litote.kmongo.coroutine.CoroutineCollection
import org.litote.kmongo.id.WrappedObjectId

fun String.toIdSecure(fieldName: String): Id<*> {
    return try {
        WrappedObjectId<Any>(this)
    } catch (e: IllegalArgumentException) {
        throw  IllegalId(fieldName, this)
    }
}


suspend fun CoroutineCollection<*>.isDocumentExists(
    filter: Bson = EMPTY_BSON,
    options: CountOptions = CountOptions(),
): Boolean {
    return countDocuments(filter, options) > 0
}


