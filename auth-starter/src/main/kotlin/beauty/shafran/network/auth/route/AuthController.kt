package beauty.shafran.network.auth.route

import beauty.shafran.network.accounts.repositories.AccountRepository
import beauty.shafran.network.accounts.route.Account
import beauty.shafran.network.auth.AuthenticationService
import beauty.shafran.network.auth.DelicateAuthorizationApi
import beauty.shafran.network.auth.RefreshAuthorizedAccount
import beauty.shafran.network.auth.token.TokenService
import org.springframework.data.repository.findByIdOrNull
import org.springframework.graphql.data.method.annotation.Argument
import org.springframework.graphql.data.method.annotation.MutationMapping
import org.springframework.graphql.data.method.annotation.SchemaMapping
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.Authentication
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.stereotype.Controller
import java.time.LocalDateTime


class AuthMutation

data class AccessTokenData(
    val token: String,
    val sessionId: String,
    val accountId: String,
    val expiresAt: LocalDateTime,
)


data class RefreshTokenData(
    val token: String,
    val tokenId: String,
    val accountId: String,
)

class TokensData(
    val sessionId: Long,
    val accountId: Long,
    val tokenId: Long,
    val expiresAt: LocalDateTime,
    val authorities: List<String>,
)


class LoginAccountInput(
    val username: String,
    val password: String,
)

class RegisterAccountInput(
    val username: String,
    val password: String,
    val name: String,
)

class RefreshTokenInput(
    val accessToken: String,
)


@Controller
private class AuthController(
    private val authenticationService: AuthenticationService,
    private val tokenService: TokenService,
    private val accountRepository: AccountRepository,
) {

    @MutationMapping
    fun auth() = AuthMutation()

    @PreAuthorize("hasAuthority(T(beauty.shafran.network.auth.Authority).SCOPE_LOGIN_ACCOUNT.authority)")
    @OptIn(DelicateAuthorizationApi::class)
    @SchemaMapping(typeName = "AuthMutation")
    fun login(@Argument input: LoginAccountInput): TokensData {
        return authenticationService.login(input.username, input.password)
    }

    @PreAuthorize("hasAuthority(T(beauty.shafran.network.auth.Authority).SCOPE_REGISTER_ACCOUNT.authority)")
    @SchemaMapping(typeName = "AuthMutation")
    fun register(@Argument input: RegisterAccountInput): TokensData {
        return authenticationService.register(input.username, input.name, input.password)
    }

    @PreAuthorize("hasAuthority(T(beauty.shafran.network.auth.Authority).SCOPE_REFRESH.authority)")
    @SchemaMapping(typeName = "AuthMutation")
    fun refresh(
        @Argument input: RefreshTokenInput,
        @AuthenticationPrincipal(errorOnInvalidType = true) account: RefreshAuthorizedAccount,
    ): TokensData {
        return authenticationService.refresh(
            accessToken = input.accessToken,
            accountId = account.accountId,
            tokenId = account.tokenId,
        )
    }

    @SchemaMapping
    fun accessToken(source: TokensData): AccessTokenData {
        return tokenService.createAccessToken(
            accountId = source.accountId,
            sessionId = source.sessionId,
            authorities = source.authorities,
            expiresAt = source.expiresAt,
        )
    }

    @SchemaMapping
    fun refreshToken(source: TokensData): RefreshTokenData {
        return tokenService.createRefreshToken(
            accountId = source.accountId,
            tokenId = source.tokenId,
        )
    }

    @SchemaMapping
    fun account(source: TokensData): Account {
        val account = accountRepository.findByIdOrNull(source.accountId) ?: TODO()
        return Account(
            id = source.accountId,
            username = account.username
        )
    }

}