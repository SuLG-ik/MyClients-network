package beauty.shafran.network.auth

import beauty.shafran.network.auth.entity.AccountSessionEntity
import beauty.shafran.network.auth.entity.collectionName
import org.litote.kmongo.coroutine.CoroutineCollection
import org.litote.kmongo.coroutine.CoroutineDatabase
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class AuthBean {

    @Bean
    fun getAccountsSessionsCollection(database: CoroutineDatabase): CoroutineCollection<AccountSessionEntity> {
        return database.getCollection(AccountSessionEntity.collectionName)
    }

}