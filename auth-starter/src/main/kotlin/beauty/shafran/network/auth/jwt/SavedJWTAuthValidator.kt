package beauty.shafran.network.auth.jwt

import beauty.shafran.network.auth.AccessedAuthorizedAccount
import beauty.shafran.network.auth.AuthorizedAccount
import beauty.shafran.network.auth.AuthorizedAccountPrincipal
import beauty.shafran.network.auth.RefreshAuthorizedAccount
import beauty.shafran.network.accounts.data.AccountSessionId
import beauty.shafran.network.auth.data.TokenId
import beauty.shafran.network.auth.token.AuthTokenService
import beauty.shafran.network.database.Transactional
import beauty.shafran.network.database.invoke
import com.auth0.jwt.interfaces.JWTVerifier
import io.ktor.server.application.*
import io.ktor.server.auth.jwt.*

internal class SavedJWTAuthValidator(
    configuration: JWTAuthenticationConfig,
    private val service: AuthTokenService,
    private val transactional: Transactional,
    override val verifier: JWTVerifier,
) : JWTAuthentication {

    override val configuration = "clients"

    override val realm: String = configuration.realm

    override val validator: suspend ApplicationCall.(JWTCredential) -> AuthorizedAccountPrincipal? = this::validate

    private suspend fun validateInternal(call: ApplicationCall, credential: JWTCredential): AuthorizedAccount? {
        val sessionId = credential.getClaim("sessionId", Long::class)
        return transactional {
            if (sessionId != null) {
                if (!service.isSessionAuthorized(AccountSessionId(sessionId)))
                    return@transactional null
                AccessedAuthorizedAccount(
                    sessionId = sessionId,
                    accountId = credential.getClaim("accountId", Long::class)!!,
                    authorities = credential.getListClaim("authorities", String::class),
                )
            } else {
                val tokenId = credential.jwtId?.toLongOrNull()!!
                if (!service.isRefreshTokenAuthorized(TokenId(tokenId)))
                    return@transactional null
                RefreshAuthorizedAccount(
                    accountId = credential.getClaim("accountId", Long::class)!!,
                    tokenId = tokenId,
                    authorities = credential.getListClaim("authorities", String::class),
                )
            }

        }

    }

    private suspend fun validate(call: ApplicationCall, credential: JWTCredential): AuthorizedAccountPrincipal? {
        return validateInternal(call, credential)?.let { AuthorizedAccountPrincipal(it) }
    }

}