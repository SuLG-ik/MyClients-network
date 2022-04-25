package beauty.shafran.network.utils

import beauty.shafran.network.IllegalId
import com.mongodb.client.model.CountOptions
import org.bson.conversions.Bson
import org.litote.kmongo.EMPTY_BSON
import org.litote.kmongo.Id
import org.litote.kmongo.coroutine.CoroutineCollection
import org.litote.kmongo.id.WrappedObjectId

fun <T> String.toIdSecure(fieldName: String): Id<T> {
    return try {
        WrappedObjectId(this)
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


