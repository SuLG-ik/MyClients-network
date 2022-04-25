package beauty.shafran.network.auth.filter

import beauty.shafran.network.auth.data.AuthorizedAccount
import beauty.shafran.network.auth.data.AuthorizedAccountData
import beauty.shafran.network.auth.token.TokenAuthService
import beauty.shafran.network.utils.SuspendConverter
import org.springframework.security.authentication.AbstractAuthenticationToken
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.stereotype.Component

@Component
class SuspendJWTConverter(
    private val tokenAuthService: TokenAuthService,
) : SuspendConverter<Jwt, AbstractAuthenticationToken>() {
    override suspend fun Jwt.convert(): AbstractAuthenticationToken {
        val sessionId = getClaimAsString("sessionsId")
        val principal = tokenAuthService.authorizeAccount(sessionId)
        return AuthorizedAccountAuthentication(principal, this)
    }
}


class AuthorizedAccountAuthentication(
    private val account: AuthorizedAccount,
    private val jwt: Jwt,
) : AbstractAuthenticationToken(account.scopes.map { SimpleGrantedAuthority(it) }) {
    override fun getName(): String {
        return account.accountId
    }


    override fun getCredentials(): Jwt {
        return jwt
    }

    override fun getDetails(): AuthorizedAccountData {
        return account.data
    }

    override fun getPrincipal(): AuthorizedAccount = account

}