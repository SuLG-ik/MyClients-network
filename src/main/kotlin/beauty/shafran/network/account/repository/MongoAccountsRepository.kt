package beauty.shafran.network.account.repository

import beauty.shafran.network.AccountAlreadyDeactivated
import beauty.shafran.network.AccountAlreadyExists
import beauty.shafran.network.AccountIllegalCredentials
import beauty.shafran.network.AccountNotExists
import beauty.shafran.network.account.entity.AccountDeactivationEntity
import beauty.shafran.network.account.entity.AccountEntity
import beauty.shafran.network.account.entity.AccountEntityData
import beauty.shafran.network.account.entity.AccountPasswordAuthEntity
import beauty.shafran.network.api.AuthPasswordEncoder
import beauty.shafran.network.utils.MetaEntity
import beauty.shafran.network.utils.MongoTransactional
import beauty.shafran.network.utils.isDocumentExists
import beauty.shafran.network.utils.toIdSecure
import org.litote.kmongo.and
import org.litote.kmongo.coroutine.CoroutineCollection
import org.litote.kmongo.div
import org.litote.kmongo.eq
import org.litote.kmongo.ne
import org.springframework.stereotype.Repository

@Repository
class MongoAccountsRepository(
    private val mongoTransactional: MongoTransactional,
    private val accountsCollection: CoroutineCollection<AccountEntity>,
    private val accountsPasswordAuthCollection: CoroutineCollection<AccountPasswordAuthEntity>,
    private val accountDeactivationEntity: CoroutineCollection<AccountDeactivationEntity>,
    private val authPasswordEncoder: AuthPasswordEncoder,
) : AccountsRepository {

    override suspend fun throwIfAccountNotExists(accountId: String) {
        if (!accountsCollection.isDocumentExists(AccountEntity::id eq accountId.toIdSecure("accountId")))
            throw AccountNotExists(accountId)
    }

    override suspend fun throwIfAccountNotExistsOrDeactivated(accountId: String) {
        throwIfAccountNotExists(accountId)
        if (accountDeactivationEntity.isDocumentExists(
                and(
                    AccountDeactivationEntity::accountId eq accountId,
                    AccountDeactivationEntity::disabling ne null
                ))
        ) {
            throw AccountAlreadyDeactivated(accountId)
        }
    }

    override suspend fun findAccountByUsernameCredential(username: String, password: String): AccountEntity {
        val account = accountsCollection.findOne(AccountEntity::data / AccountEntityData::username eq username)
            ?: throw AccountNotExists(username)
        val auth = accountsPasswordAuthCollection.find(AccountPasswordAuthEntity::accountId eq account.id.toString())
            .descendingSort(AccountPasswordAuthEntity::meta / MetaEntity::creationDate)
            .limit(1)
            .first() ?: throw IllegalStateException("Auth cant be empty")
        if (!authPasswordEncoder.match(password, auth.password))
            throw AccountIllegalCredentials(username)
        return account
    }

    override suspend fun createAccount(data: AccountEntityData, password: String): AccountEntity {
        if (accountsCollection.isDocumentExists(AccountEntity::data / AccountEntityData::username eq data.username))
            throw AccountAlreadyExists(data.username)

        val account = AccountEntity(data = data)
        val accountAuth = AccountPasswordAuthEntity(
            password = authPasswordEncoder.encode(password),
            accountId = account.id.toString(),
        )
        accountsCollection.insertOne(account)
        accountsPasswordAuthCollection.insertOne(accountAuth)
        return account
    }

    override suspend fun changePassword(accountId: String, oldPassword: String, newPassword: String): AccountEntity {
        throwIfAccountNotExistsOrDeactivated(accountId)
        val auth = accountsPasswordAuthCollection.find(AccountPasswordAuthEntity::accountId eq accountId)
            .descendingSort(AccountPasswordAuthEntity::meta / MetaEntity::creationDate)
            .limit(1)
            .first() ?: throw IllegalStateException("Auth cant be empty")
        if (!authPasswordEncoder.match(oldPassword, auth.password))
            throw AccountIllegalCredentials(accountId)

        val accountAuth = AccountPasswordAuthEntity(
            password = authPasswordEncoder.encode(newPassword),
            accountId = accountId,
        )
        accountsPasswordAuthCollection.insertOne(accountAuth)
        return findAccountById(accountId)
    }

    override suspend fun findAccountById(accountId: String): AccountEntity {
        return accountsCollection.findOneById(AccountEntity::id eq accountId.toIdSecure("accountId"))
            ?: throw AccountNotExists(accountId)
    }

}