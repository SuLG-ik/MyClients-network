package beauty.shafran.network.account.repository

import beauty.shafran.network.account.data.AccountId
import beauty.shafran.network.account.entity.AccountDataEntity
import beauty.shafran.network.account.entity.AccountEntity
import beauty.shafran.network.utils.TransactionalScope

interface AccountsRepository {
    suspend fun TransactionalScope.isAccountExists(accountId: AccountId): Boolean

    suspend fun TransactionalScope.throwIfAccountNotExists(accountId: AccountId)

    suspend fun TransactionalScope.throwIfAccountNotExistsOrDeactivated(accountId: AccountId)

    suspend fun TransactionalScope.findAccountByUsernameCredential(
        username: String,
        password: String,
    ): AccountEntity

    suspend fun TransactionalScope.createAccount(username: String, password: String): AccountEntity

    suspend fun TransactionalScope.changePassword(
        accountId: AccountId,
        oldPassword: String,
        newPassword: String,
    ): AccountEntity

    suspend fun TransactionalScope.findAccountById(accountId: AccountId): AccountEntity

    suspend fun TransactionalScope.findAccountDataById(accountId: AccountId): AccountDataEntity

}