package beauty.shafran.network.auth.repository

import beauty.shafran.network.account.data.AccountId
import beauty.shafran.network.auth.data.AuthorizedAccount
import beauty.shafran.network.auth.entity.*
import beauty.shafran.network.utils.TransactionalScope

interface PermissionsRepository {

    fun TransactionalScope.isAccessedForAccount(
        permission: LabeledPermission,
        permissions: ReferencedPermissions,
    ): Boolean

    fun TransactionalScope.isAccessedForAccount(permission: IdPermission, permissions: ReferencedPermissions): Boolean

    fun TransactionalScope.isAccessedForAccount(permission: IdPermission, account: AuthorizedAccount): Boolean

    fun TransactionalScope.getPermissionsForAccount(accountId: AccountId): PermissionEntitiesToAccountEntity

    fun TransactionalScope.getPermissionsForSession(sessionId: AccountSessionId): PermissionEntitiesToSessionEntity

    fun TransactionalScope.isAccessedForAccount(permission: LabeledPermission, account: AuthorizedAccount): Boolean
}