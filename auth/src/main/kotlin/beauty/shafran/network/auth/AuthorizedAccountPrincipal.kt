package beauty.shafran.network.auth

import beauty.shafran.network.AccessDenied
import io.ktor.server.auth.*

class AuthorizedAccountPrincipal(
    val account: AuthorizedAccount,
) : Principal

abstract class AuthorizedAccount {
    abstract val accountId: Long
    abstract val authorities: List<String>
}

class AccessedAuthorizedAccount(
    override val accountId: Long,
    val sessionId: Long,
    override val authorities: List<String>,
) : AuthorizedAccount()

class RefreshAuthorizedAccount(
    override val accountId: Long,
    val tokenId: Long,
    override val authorities: List<String>,
) : AuthorizedAccount()

fun AuthorizedAccount.hasAuthority(authority: String): Boolean {
    return authority in authorities
}

fun AuthorizedAccount.hasAuthorityStrict(authority: String): Boolean {
    if (!hasAuthority(authority))
        throw AccessDenied()
    return true
}