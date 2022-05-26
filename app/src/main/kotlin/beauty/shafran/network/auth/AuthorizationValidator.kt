package beauty.shafran.network.auth

import beauty.shafran.AccessDenied
import beauty.shafran.network.auth.data.AuthorizedAccount
import beauty.shafran.network.auth.entity.PermissionEntity
import beauty.shafran.network.companies.data.CompanyId
import beauty.shafran.network.companies.entity.AccessScope

class AlwaysValid : AuthorizationValidator {
    override fun hasAccess(scope: AuthorizationScope, account: AuthorizedAccount): Boolean {
        return true
    }

}

class AuthorizationValidatorImpl : AuthorizationValidator {

    override fun hasAccess(scope: AuthorizationScope, account: AuthorizedAccount): Boolean {
        return scope.hasAccess(account.permissions)
    }
}

interface AuthorizationValidator {

    fun hasAccess(scope: AuthorizationScope, account: AuthorizedAccount): Boolean

}

inline fun AuthorizationValidator.throwIfNotAccessed(
    scope: AuthorizationScope,
    account: AuthorizedAccount,
    cause: () -> Exception = { AccessDenied() },
) {
    if (!hasAccess(scope, account)) throw cause()
}

inline fun AuthorizationValidator.throwIfNotAccessedForCompany(
    companyId: CompanyId,
    scope: AccessScope,
    account: AuthorizedAccount,
    cause: () -> Exception = { AccessDenied() },
) {
    if (!hasAccessForCompany(companyId, scope, account)) throw cause()
}

fun AuthorizationValidator.hasAccessForCompany(
    companyId: CompanyId,
    scope: AccessScope,
    account: AuthorizedAccount,
): Boolean {
    return hasAccess(CompanyAuthorizationScope(companyId, scope), account)
}

sealed class AuthorizationScope {
    abstract fun hasAccess(scopes: List<PermissionEntity>): Boolean
}

class CompanyAuthorizationScope(
    val companyId: CompanyId,
    val scope: AccessScope,
) : AuthorizationScope() {
    override fun hasAccess(scopes: List<PermissionEntity>): Boolean {
        return "${scope.code}.${companyId.id}" in scopes.map { it.label.label }
    }

}