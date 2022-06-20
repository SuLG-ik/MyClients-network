package beauty.shafran.network.auth

import org.springframework.security.authentication.AbstractAuthenticationToken
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder


sealed class AuthorizedAccount(
    val accountId: Long,
    val authorities: List<String>,
) {

    companion object {
        fun get() = AuthorizedAuthentication.get().account

    }
}

class AccessAuthorizedAccount(accountId: Long, val sessionId: Long, authority: List<String>) :
    AuthorizedAccount(accountId = accountId, authorities = authority)

class RefreshAuthorizedAccount(accountId: Long, val tokenId: Long, authority: List<String>) :
    AuthorizedAccount(accountId = accountId, authorities = authority)

sealed class AuthorizedAuthentication(
    authorities: List<String>,
) : AbstractAuthenticationToken(authorities.map { SimpleGrantedAuthority(it) }) {


    abstract val token: String
    abstract val account: AuthorizedAccount

    override fun getCredentials(): Any {
        return token
    }

    override fun getPrincipal(): AuthorizedAccount {
        return account
    }

    companion object {
        fun get() = SecurityContextHolder.getContext().authentication as AuthorizedAuthentication
    }

}

data class AccessAuthorizedAuthentication(override val token: String, override val account: AccessAuthorizedAccount) :
    AuthorizedAuthentication(account.authorities) {
    override fun getName(): String = toString()

}

class RefreshAuthorizedAuthentication(override val token: String, override val account: RefreshAuthorizedAccount) :
    AuthorizedAuthentication(account.authorities) {
    override fun getName(): String = toString()

}
