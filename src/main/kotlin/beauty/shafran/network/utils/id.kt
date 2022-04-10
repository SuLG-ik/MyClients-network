package beauty.shafran.network.utils

import beauty.shafran.network.IllegalId
import org.bson.types.ObjectId
import org.litote.kmongo.Id
import org.litote.kmongo.id.WrappedObjectId

fun <T> String.toIdSecure(fieldName: String): Id<T> {
    return try {
        WrappedObjectId(this)
    } catch (e: IllegalArgumentException) {
        throw  IllegalId(fieldName, this)
    }
}