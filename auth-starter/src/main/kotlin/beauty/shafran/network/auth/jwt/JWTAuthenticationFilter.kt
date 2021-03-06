package beauty.shafran.network.auth.jwt

import beauty.shafran.network.auth.*
import beauty.shafran.network.auth.token.AccessToken
import beauty.shafran.network.auth.token.RefreshToken
import beauty.shafran.network.auth.token.TokenService
import org.springframework.security.authentication.AnonymousAuthenticationToken
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
internal class JWTAuthenticationFilter(
    private val authenticationManager: AuthenticationManager,
    private val authorizationService: AuthorizationService,
    private val tokenService: TokenService,
) : OncePerRequestFilter() {

    private fun attemptAuthentication(request: HttpServletRequest): Authentication? {
        val authorization = request.getHeader("Authorization")
        val clientId = request.getHeader("Client")
        if (clientId.isNullOrBlank())
            TODO()
        if (authorization.isNullOrBlank()) {
            return AnonymousAuthenticationToken(clientId, "Anon", authorizationService.anonymousTokenAuthorities())
        }
        if (!authorization.startsWith("Bearer"))
            TODO()
        val authentication = when (val jwt = tokenService.decodeAbstractToken(authorization.removePrefix("Bearer "))) {
            is AccessToken -> AccessAuthorizedAuthentication(
                token = jwt.token,
                AccessAuthorizedAccount(
                    accountId = jwt.accountId,
                    sessionId = jwt.sessionId,
                    authority = jwt.authority
                )
            )

            is RefreshToken -> RefreshAuthorizedAuthentication(
                token = jwt.token,
                RefreshAuthorizedAccount(
                    accountId = jwt.accountId,
                    tokenId = jwt.tokenId,
                    authority = jwt.authority
                )
            )
        }
        return authenticationManager.authenticate(authentication)
    }

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain,
    ) {
        val authentication = attemptAuthentication(request)
        SecurityContextHolder.getContext().authentication = authentication
        filterChain.doFilter(request, response)
    }

}