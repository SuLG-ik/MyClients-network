package beauty.shafran.network.auth.repository

import beauty.shafran.SessionNotExists
import beauty.shafran.network.account.data.AccountId
import beauty.shafran.network.auth.entity.AccountSessionEntity
import beauty.shafran.network.auth.entity.AccountSessionId
import beauty.shafran.network.auth.entity.AccountSessionTable
import beauty.shafran.network.auth.entity.DeviceType
import beauty.shafran.network.utils.TransactionalScope
import beauty.shafran.network.utils.selectLatest
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.insertAndGetId


class PostgresAccountSessionsRepository(
    private val db: Database,
) : AccountSessionsRepository {

    override suspend fun TransactionalScope.createSessionForAccount(accountId: AccountId): AccountSessionEntity {
        val sessionId = AccountSessionTable.insertAndGetId {
            it[this.accountId] = accountId.id
            it[this.deviceType] = DeviceType.UNKNOWN
        }
        return AccountSessionEntity(
            id = AccountSessionId(sessionId.value),
            accountId = accountId,
        )
    }

    override suspend fun TransactionalScope.findSessionWithId(sessionId: AccountSessionId): AccountSessionEntity {
        val session =
            AccountSessionTable.selectLatest { AccountSessionTable.id eq sessionId.id }
                ?: throw SessionNotExists(sessionId.toString())
        return AccountSessionEntity(
            id = sessionId,
            accountId = AccountId(session[AccountSessionTable.accountId].value),
        )
    }
}