package beauty.shafran.network.account.repository

import beauty.shafran.network.account.data.AccountId
import beauty.shafran.network.account.data.AccountUsername
import beauty.shafran.network.account.entity.AccountDataEntity
import beauty.shafran.network.account.entity.AccountEntity
import beauty.shafran.network.utils.TransactionalScope

interface AccountsRepository {

    context (TransactionalScope) suspend fun isAccountExists(username: AccountUsername): Boolean

    context (TransactionalScope) suspend fun isAccountExists(accountId: AccountId): Boolean

    context (TransactionalScope) suspend fun throwIfAccountNotExists(accountId: AccountId)

    context (TransactionalScope) suspend fun throwIfAccountNotExistsOrDeactivated(accountId: AccountId)

    context (TransactionalScope) suspend fun findAccountByUsernameCredential(
        username: String,
        password: String,
    ): AccountEntity

    context (TransactionalScope) suspend fun createAccount(username: String, password: String): AccountEntity

    context (TransactionalScope) suspend fun changePassword(
        accountId: AccountId,
        oldPassword: String,
        newPassword: String,
    ): AccountEntity

    context (TransactionalScope) suspend fun findAccountById(accountId: AccountId): AccountEntity

    context (TransactionalScope) suspend fun findAccountDataById(accountId: AccountId): AccountDataEntity

}