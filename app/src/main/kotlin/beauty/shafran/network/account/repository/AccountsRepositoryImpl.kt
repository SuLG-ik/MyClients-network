package beauty.shafran.network.account.repository

import beauty.shafran.AccountIllegalCredentials
import beauty.shafran.AccountNotExists
import beauty.shafran.network.account.data.AccountId
import beauty.shafran.network.account.data.AccountUsername
import beauty.shafran.network.account.entity.*
import beauty.shafran.network.api.AuthPasswordEncoder
import beauty.shafran.network.utils.TransactionalScope
import beauty.shafran.network.utils.isRowExists
import beauty.shafran.network.utils.selectLatest
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

class AccountsRepositoryImpl(
    private val authPasswordEncoder: AuthPasswordEncoder,
) : AccountsRepository {

    context(TransactionalScope) override suspend fun isAccountExists(username: AccountUsername): Boolean {
        return AccountTable.isRowExists { AccountTable.username eq username.username }
    }

    context (TransactionalScope) override suspend fun isAccountExists(accountId: AccountId): Boolean {
        return AccountTable.isRowExists { AccountTable.id eq accountId.id }
    }

    context (TransactionalScope) override suspend fun throwIfAccountNotExists(accountId: AccountId) {
        if (isAccountExists(accountId)) throw AccountNotExists(accountId.id.toString())
    }

    context (TransactionalScope) override suspend fun throwIfAccountNotExistsOrDeactivated(accountId: AccountId) {
        throwIfAccountNotExists(accountId)
    }

    context (TransactionalScope) override suspend fun findAccountByUsernameCredential(
        username: String,
        password: String,
    ): AccountEntity {
        val account = AccountTable.selectLatest { AccountTable.username eq username }?.toAccountEntity()
            ?: throw AccountNotExists(username)
        val hashedPassword = AccountPasswordCredentialTable.selectLatest {
            AccountPasswordCredentialTable.accountId eq account.id
        }?.get(AccountPasswordCredentialTable.password) ?: throw AccountNotExists(username)
        if (!authPasswordEncoder.match(password, hashedPassword))
            throw AccountIllegalCredentials(username)
        return account
    }

    context (TransactionalScope) override suspend fun createAccount(username: String, password: String): AccountEntity {
        val currentDate = Clock.System.now().toLocalDateTime(TimeZone.UTC)
        val account = AccountTable.insertAndGetEntity(username = username, creationDate = currentDate)
        AccountPasswordCredentialTable.insertEntity(
            accountId = account.id,
            password = authPasswordEncoder.encode(password)
        )
        return account
    }

    context (TransactionalScope) override suspend fun changePassword(
        accountId: AccountId,
        oldPassword: String,
        newPassword: String,
    ): AccountEntity {
        val account = findAccountById(accountId)
        val hashedPassword = AccountPasswordCredentialTable.selectLatest {
            AccountPasswordCredentialTable.accountId eq accountId.id
        }?.get(AccountPasswordCredentialTable.password) ?: throw AccountNotExists(accountId.id.toString())
        if (!authPasswordEncoder.match(oldPassword, hashedPassword)
        ) throw AccountIllegalCredentials(accountId.id.toString())
        return account
    }

    context (TransactionalScope) override suspend fun findAccountById(accountId: AccountId): AccountEntity {
        return AccountTable.findById(accountId.id) ?: throw AccountNotExists(accountId.id.toString())
    }

    context (TransactionalScope) override suspend fun findAccountDataById(accountId: AccountId): AccountDataEntity {
        return AccountDataTable.selectLatest { AccountDataTable.accountId eq accountId.id }?.toAccountDataEntity()
            ?: throw AccountNotExists(accountId.id.toString())
    }
}