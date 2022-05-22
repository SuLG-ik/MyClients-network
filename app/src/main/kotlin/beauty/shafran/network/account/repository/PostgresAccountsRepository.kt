package beauty.shafran.network.account.repository

import beauty.shafran.AccountIllegalCredentials
import beauty.shafran.AccountNotExists
import beauty.shafran.network.account.data.AccountId
import beauty.shafran.network.account.entity.*
import beauty.shafran.network.api.AuthPasswordEncoder
import beauty.shafran.network.utils.TransactionalScope
import beauty.shafran.network.utils.isRowExists
import beauty.shafran.network.utils.selectLatest
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.koin.core.annotation.Single

@Single
class PostgresAccountsRepository(
    private val authPasswordEncoder: AuthPasswordEncoder,
) : AccountsRepository {

    override suspend fun TransactionalScope.isAccountExists(accountId: AccountId): Boolean {
        return AccountTable.isRowExists { AccountTable.id eq accountId.id }
    }

    override suspend fun TransactionalScope.throwIfAccountNotExists(accountId: AccountId) {
        if (isAccountExists(accountId)) throw AccountNotExists(accountId.id.toString())
    }

    override suspend fun TransactionalScope.throwIfAccountNotExistsOrDeactivated(accountId: AccountId) {
        throwIfAccountNotExists(accountId)
    }

    override suspend fun TransactionalScope.findAccountByUsernameCredential(
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

    override suspend fun TransactionalScope.createAccount(username: String, password: String): AccountEntity {
        val currentDate = Clock.System.now().toLocalDateTime(TimeZone.UTC)
        val accountId = AccountTable.insertEntityAndGetId(username = username, creationDate = currentDate)
        AccountPasswordCredentialTable.insertEntity(
            accountId = accountId,
            password = authPasswordEncoder.encode(password)
        )
        return AccountEntity(
            id = accountId,
            username = username,
            creationDate = currentDate,
        )
    }

    override suspend fun TransactionalScope.changePassword(
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

    override suspend fun TransactionalScope.findAccountById(accountId: AccountId): AccountEntity {
        return AccountTable.findById(accountId.id) ?: throw AccountNotExists(accountId.id.toString())
    }

    override suspend fun TransactionalScope.findAccountDataById(accountId: AccountId): AccountDataEntity {
        return AccountDataTable.selectLatest { AccountDataTable.accountId eq accountId.id }?.toAccountDataEntity()
            ?: throw AccountNotExists(accountId.id.toString())
    }
}