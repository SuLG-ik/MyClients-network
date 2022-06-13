package beauty.shafran.network.auth.repository

import beauty.shafran.network.accounts.data.AccountId
import beauty.shafran.network.auth.data.AccountPassword
import beauty.shafran.network.auth.encoder.PasswordEncoder
import beauty.shafran.network.auth.tables.AccountPasswordTable
import beauty.shafran.network.auth.tables.insertAndGetEntity
import beauty.shafran.network.auth.tables.toAccountPasswordEntity
import beauty.shafran.network.database.TransactionalScope
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.update

internal class AuthPasswordRepositoryImpl(
    private val passwordEncoder: PasswordEncoder,
) : AuthPasswordRepository {

    context (TransactionalScope) override suspend fun matchPassword(
        accountId: AccountId,
        rawPassword: AccountPassword,
    ): Boolean {
        val passwordEntity = AccountPasswordTable.select { AccountPasswordTable.accountId eq accountId.value }
            .firstOrNull()
            ?.toAccountPasswordEntity() ?: TODO("UNAVAILABLE SITUATION")
        return passwordEncoder.matches(rawPassword = rawPassword.value, passwordHash = passwordEntity.passwordHash)
    }

    context(TransactionalScope) override suspend fun setPassword(
        accountId: AccountId,
        rawPassword: AccountPassword,
    ) {
        val passwordEntity = AccountPasswordTable.select { AccountPasswordTable.accountId eq accountId.value }
            .firstOrNull()?.toAccountPasswordEntity()
        val passwordHash = passwordEncoder.hashPassword(rawPassword.value)
        if (passwordEntity == null) {
            AccountPasswordTable.insertAndGetEntity(accountId = accountId.value, passwordHash = passwordHash)
        } else {
            AccountPasswordTable.update {
                it[this.id] = passwordEntity.id
                it[this.passwordHash] = passwordHash
            }
        }
    }
}