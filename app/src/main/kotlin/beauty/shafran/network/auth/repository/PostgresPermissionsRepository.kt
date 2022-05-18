package beauty.shafran.network.auth.repository

import beauty.shafran.network.account.data.AccountId
import beauty.shafran.network.auth.data.AuthorizedAccount
import beauty.shafran.network.auth.entity.*
import beauty.shafran.network.utils.TransactionalScope
import org.jetbrains.exposed.sql.select
import org.koin.core.annotation.Single

@Single
class PostgresPermissionsRepository : PermissionsRepository {

    override fun TransactionalScope.isAccessedForAccount(
        permission: LabeledPermission,
        permissions: ReferencedPermissions,
    ): Boolean {
        return permission.label in permissions.labels
    }

    override fun TransactionalScope.isAccessedForAccount(
        permission: IdPermission,
        permissions: ReferencedPermissions,
    ): Boolean {
        return permission.id in permissions.ids
    }

    override fun TransactionalScope.isAccessedForAccount(
        permission: LabeledPermission,
        account: AuthorizedAccount,
    ): Boolean {
        return isAccessedForAccount(
            permission,
            PermissionEntitiesToAccountEntity(accountId = account.accountId, account.permissions)
        )
    }

    override fun TransactionalScope.isAccessedForAccount(
        permission: IdPermission,
        account: AuthorizedAccount,
    ): Boolean {
        return isAccessedForAccount(
            permission,
            PermissionEntitiesToAccountEntity(accountId = account.accountId, account.permissions)
        )
    }

    override fun TransactionalScope.getPermissionsForAccount(accountId: AccountId): PermissionEntitiesToAccountEntity {
        val ids =
            PermissionToAccountTable.select { PermissionToAccountTable.accountId eq accountId.id }
                .map { it[PermissionToAccountTable.permissionId].value }
        return PermissionEntitiesToAccountEntity(
            accountId = accountId,
            permissions = getPermissionsByIds(ids)
        )
    }

    private fun TransactionalScope.getPermissionsByIds(ids: List<Long>): List<PermissionEntity> {
        return PermissionTable.select { PermissionTable.id inList ids }
            .map {
                PermissionEntity(
                    label = PermissionLabel(it[PermissionTable.label]),
                    id = PermissionId(it[PermissionTable.id].value)
                )
            }
    }


    override fun TransactionalScope.getPermissionsForSession(sessionId: AccountSessionId): PermissionEntitiesToSessionEntity {
        val ids =
            PermissionToSessionTable.select { PermissionToSessionTable.sessionId eq sessionId.id }
                .map { it[PermissionToSessionTable.permissionId].value }
        return PermissionEntitiesToSessionEntity(
            sessionId = sessionId,
            permissions = getPermissionsByIds(ids)
        )
    }
}