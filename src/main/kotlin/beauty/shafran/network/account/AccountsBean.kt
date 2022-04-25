package beauty.shafran.network.account

import beauty.shafran.network.account.entity.AccountDeactivationEntity
import beauty.shafran.network.account.entity.AccountEntity
import beauty.shafran.network.account.entity.AccountPasswordAuthEntity
import beauty.shafran.network.account.entity.collectionName
import org.litote.kmongo.coroutine.CoroutineCollection
import org.litote.kmongo.coroutine.CoroutineDatabase
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class AccountsBean {

    @Bean
    fun getAccountsCollection(database: CoroutineDatabase): CoroutineCollection<AccountEntity> {
        return database.getCollection(AccountEntity.collectionName)
    }

    @Bean
    fun getAccountsPasswordAuthCollection(database: CoroutineDatabase): CoroutineCollection<AccountPasswordAuthEntity> {
        return database.getCollection(AccountPasswordAuthEntity.collectionName)
    }

    @Bean
    fun getAccountsDeactivationsCollection(database: CoroutineDatabase): CoroutineCollection<AccountDeactivationEntity> {
        return database.getCollection(AccountDeactivationEntity.collectionName)
    }

}