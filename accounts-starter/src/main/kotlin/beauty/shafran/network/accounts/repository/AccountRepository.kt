package beauty.shafran.network.accounts.repository

import beauty.shafran.network.accounts.data.AccountId
import beauty.shafran.network.accounts.data.AccountUsername
import beauty.shafran.network.accounts.tables.AccountDataEntity
import beauty.shafran.network.accounts.tables.AccountEntity
import beauty.shafran.network.database.TransactionalScope

interface AccountRepository {

    context(TransactionalScope) suspend fun findAccount(username: AccountUsername): AccountEntity

    context(TransactionalScope) suspend fun findAccount(id: AccountId): AccountEntity

    context(TransactionalScope) suspend fun saveAccount(username: AccountUsername, name: String): AccountEntity

    context(TransactionalScope) suspend fun findAccountData(id: AccountId): AccountDataEntity

    context(TransactionalScope) suspend fun findAccountAndData(id: AccountId): Pair<AccountEntity, AccountDataEntity>

}