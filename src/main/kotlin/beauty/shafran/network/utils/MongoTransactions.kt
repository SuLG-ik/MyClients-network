package beauty.shafran.network.utils

import com.mongodb.reactivestreams.client.ClientSession
import org.litote.kmongo.coroutine.CoroutineClient
import org.springframework.stereotype.Service

interface MongoTransactional {

    suspend fun withTransaction(transaction: suspend (ClientSession) -> Unit)

}


@Service
class MongoTransactionalImpl(
    val mongoClient: CoroutineClient,
) : MongoTransactional {
    override suspend fun withTransaction(transaction: suspend (ClientSession) -> Unit) {
        mongoClient.startSession().use { transaction(it) }
    }

}