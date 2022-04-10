package beauty.shafran.network

import beauty.shafran.network.config.MongoClientConfig
import beauty.shafran.network.config.toSettings
import org.litote.kmongo.coroutine.CoroutineDatabase
import org.litote.kmongo.coroutine.coroutine
import org.litote.kmongo.reactivestreams.KMongo
import org.springframework.beans.factory.config.ConfigurableBeanFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Scope

@Configuration
class KMongoConfiguration {

    @Bean
    @Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
    fun database(config: MongoClientConfig): CoroutineDatabase {
        return KMongo.createClient(config.toSettings())
            .getDatabase(config.database)
            .coroutine
    }

}