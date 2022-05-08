package beauty.shafran.network

import beauty.shafran.network.config.MongoClientConfig
import beauty.shafran.network.config.toSettings
import org.koin.core.annotation.Module
import org.koin.core.annotation.Single
import org.litote.kmongo.coroutine.CoroutineClient
import org.litote.kmongo.coroutine.CoroutineDatabase
import org.litote.kmongo.coroutine.coroutine
import org.litote.kmongo.reactivestreams.KMongo

@Module
class KMongoCModule {

    @Single
    fun client(config: MongoClientConfig): CoroutineClient {
        return KMongo.createClient(config.toSettings()).coroutine
    }

    @Single
    fun database(client: CoroutineClient, config: MongoClientConfig): CoroutineDatabase {
        return client.getDatabase(config.database)
    }

    @Single
    fun config(): MongoClientConfig {
        return MongoClientConfig()
    }


}