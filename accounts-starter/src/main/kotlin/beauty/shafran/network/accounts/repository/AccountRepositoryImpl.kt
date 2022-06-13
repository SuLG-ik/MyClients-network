package beauty.shafran.network.accounts.repository

import beauty.shafran.network.accounts.tables.*
import beauty.shafran.network.accounts.data.AccountId
import beauty.shafran.network.accounts.data.AccountUsername
import beauty.shafran.network.accounts.data.AccountWithIdDoesNotExists
import beauty.shafran.network.accounts.data.AccountWithUsernameDoesNotExists
import beauty.shafran.network.database.TransactionalScope
import org.jetbrains.exposed.sql.JoinType
import org.jetbrains.exposed.sql.select

internal class AccountRepositoryImpl : AccountRepository {

    context (TransactionalScope) override suspend fun saveAccount(
        username: AccountUsername,
        name: String,
    ): AccountEntity {
        val account = AccountTable.insertAndGetEntity(username = username.value)
        AccountDataTable.insertEntity(name = name, accountId = account.id)
        return account
    }

    context(TransactionalScope) override suspend fun findAccount(username: AccountUsername): AccountEntity {
        return AccountTable.select { AccountTable.username eq username.value }.firstOrNull()?.toAccountEntity()
            ?: throw AccountWithUsernameDoesNotExists(username)
    }

    context(TransactionalScope) override suspend fun findAccount(id: AccountId): AccountEntity {
        return AccountTable.findById(id.value)
            ?: throw AccountWithIdDoesNotExists(id)
    }

    context(TransactionalScope) override suspend fun findAccountAndData(id: AccountId): Pair<AccountEntity, AccountDataEntity> {
        return AccountTable.join(
            otherTable = AccountDataTable,
            joinType = JoinType.RIGHT,
            additionalConstraint = { AccountTable.id eq AccountDataTable.accountId },
        )
            .select { AccountTable.id eq id.value }
            .firstOrNull()
            ?.let { row -> row.toAccountEntity() to row.toAccountDataEntity() }
            ?: throw AccountWithIdDoesNotExists(id)
    }

    context(TransactionalScope) override suspend fun findAccountData(id: AccountId): AccountDataEntity {
        return AccountDataTable.select { AccountDataTable.accountId eq id.value }
            .firstOrNull()?.toAccountDataEntity() ?: throw AccountWithIdDoesNotExists(id)
    }

}