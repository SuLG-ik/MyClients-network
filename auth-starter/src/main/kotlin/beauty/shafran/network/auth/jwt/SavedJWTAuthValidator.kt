package beauty.shafran.network.auth.jwt

import beauty.shafran.network.auth.AccessAuthorizedAuthentication
import beauty.shafran.network.auth.AuthorizedAuthentication
import beauty.shafran.network.auth.RefreshAuthorizedAuthentication
import beauty.shafran.network.auth.repository.AccountRefreshTokenRepository
import beauty.shafran.network.auth.repository.AccountSessionsRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.http.HttpStatus
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.authentication.ProviderNotFoundException
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Component
import org.springframework.web.server.ResponseStatusException

@Component
internal class JWTAuthenticationProvider(
    val sessionsRepository: AccountSessionsRepository,
    val refreshTokensRepository: AccountRefreshTokenRepository,
) : AuthenticationManager {
    @org.springframework.transaction.annotation.Transactional
    override fun authenticate(authentication: Authentication): AuthorizedAuthentication {
        return when (val jwt = authentication as? AuthorizedAuthentication) {
            is AccessAuthorizedAuthentication -> {
                val session = sessionsRepository.findByIdOrNull(jwt.account.sessionId)
                if (session?.isDeactivated != false)
                    throw BadCredentialsException("Session does not exists or deactivated")
                jwt
            }

            is RefreshAuthorizedAuthentication -> {
                val refresh = refreshTokensRepository.findByIdOrNull(jwt.account.tokenId)
                    ?: throw ResponseStatusException(HttpStatus.FORBIDDEN)
                if (refresh.id != jwt.account.tokenId) {
                    throw BadCredentialsException("Refresh token already expired")
                }
                jwt
            }

            null -> {
                throw ProviderNotFoundException("Can't encode authentication")
            }
        }
    }
}
