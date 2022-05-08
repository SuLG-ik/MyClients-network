package beauty.shafran.network.utils

import com.mongodb.reactivestreams.client.ClientSession
import org.koin.core.annotation.Single
import org.litote.kmongo.coroutine.CoroutineClient

interface MongoTransactional {

    suspend fun withTransaction(transaction: suspend (ClientSession) -> Unit)

}


@Single
class MongoTransactionalImpl(
    val mongoClient: CoroutineClient,
) : MongoTransactional {
    override suspend fun withTransaction(transaction: suspend (ClientSession) -> Unit) {
        mongoClient.startSession().use { transaction(it) }
    }

}