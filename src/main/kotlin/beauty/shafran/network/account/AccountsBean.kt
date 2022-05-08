package beauty.shafran.network.account

import beauty.shafran.network.account.entity.AccountDeactivationEntity
import beauty.shafran.network.account.entity.AccountEntity
import beauty.shafran.network.account.entity.AccountPasswordAuthEntity
import org.koin.core.annotation.Module
import org.koin.core.annotation.Named
import org.koin.core.annotation.Single
import org.litote.kmongo.coroutine.CoroutineCollection
import org.litote.kmongo.coroutine.CoroutineDatabase

@Module
class AccountsBean {

    @Single
    @Named(AccountEntity.collectionName)
    fun getAccountsCollection(database: CoroutineDatabase): CoroutineCollection<AccountEntity> {
        return database.getCollection(AccountEntity.collectionName)
    }

    @Single
    @Named(AccountPasswordAuthEntity.collectionName)
    fun getAccountsPasswordAuthCollection(database: CoroutineDatabase): CoroutineCollection<AccountPasswordAuthEntity> {
        return database.getCollection(AccountPasswordAuthEntity.collectionName)
    }

    @Single
    @Named(AccountDeactivationEntity.collectionName)
    fun getAccountsDeactivationsCollection(database: CoroutineDatabase): CoroutineCollection<AccountDeactivationEntity> {
        return database.getCollection(AccountDeactivationEntity.collectionName)
    }

}