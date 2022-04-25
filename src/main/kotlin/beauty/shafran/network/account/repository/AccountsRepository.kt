package beauty.shafran.network.account.repository

import beauty.shafran.network.account.entity.AccountEntity
import beauty.shafran.network.account.entity.AccountEntityData

interface AccountsRepository {

    suspend fun throwIfAccountNotExists(accountId: String)


    suspend fun throwIfAccountNotExistsOrDeactivated(accountId: String)

    suspend fun findAccountByUsernameCredential(
        username: String,
        password: String,
    ): AccountEntity

    suspend fun createAccount(data: AccountEntityData, password: String): AccountEntity

    suspend fun changePassword(accountId: String, oldPassword: String, newPassword: String): AccountEntity

    suspend fun findAccountById(accountId: String): AccountEntity
}