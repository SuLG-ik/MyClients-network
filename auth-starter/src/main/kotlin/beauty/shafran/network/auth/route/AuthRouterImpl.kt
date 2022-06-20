package beauty.shafran.network.auth.route

import beauty.shafran.network.accounts.entities.AccountDataEntity
import beauty.shafran.network.accounts.entities.AccountEntity
import beauty.shafran.network.accounts.repositories.AccountDataEntityRepository
import beauty.shafran.network.accounts.repositories.AccountRepository
import beauty.shafran.network.auth.AuthorizationService
import beauty.shafran.network.auth.DelicateAuthorizationApi
import beauty.shafran.network.auth.RefreshAuthorizedAccount
import beauty.shafran.network.auth.data.*
import beauty.shafran.network.auth.entities.AccountRefreshTokenEntity
import beauty.shafran.network.auth.entities.AccountSessionEntity
import beauty.shafran.network.auth.repository.AccountRefreshTokensRepository
import beauty.shafran.network.auth.repository.AccountSessionsRepository
import beauty.shafran.network.auth.repository.AuthPasswordRepository
import beauty.shafran.network.auth.token.TokenService
import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/auth")
internal class AuthRouterImpl(
    private val accountRepository: AccountRepository,
    private val accountDataRepository: AccountDataEntityRepository,
    private val authPassword: AuthPasswordRepository,
    private val tokenService: TokenService,
    private val sessionsRepository: AccountSessionsRepository,
    private val refreshTokensRepository: AccountRefreshTokensRepository,
    private val authorizationService: AuthorizationService,
) : AuthRouter {

    @PreAuthorize("hasAnyAuthority('SCOPE_refresh')")
    @Transactional
    @PostMapping("/refresh")
    override fun refresh(
        @RequestBody request: RefreshTokenRequest,
        @AuthenticationPrincipal(errorOnInvalidType = true) account: RefreshAuthorizedAccount,
    ): RefreshTokenResponse {
        val token = tokenService.decodeExpiredAccessToken(request.accessToken)
        if (token.accountId != account.accountId)
            TODO()
        val session = sessionsRepository.findByIdOrNull(token.sessionId)
        if (session?.isDeactivated != false)
            TODO()
        return RefreshTokenResponse(
            token = TokensData(
                accessToken = tokenService.createAccessToken(
                    accountId = account.accountId,
                    sessionId = session.id,
                    authorities = token.authority
                ),
                refreshToken = tokenService.createRefreshToken(
                    accountId = account.accountId,
                    tokenId = account.tokenId,
                ),
            )
        )
    }

    @DelicateAuthorizationApi
    @Transactional
    @PostMapping("/login")
    override fun login(@RequestBody request: LoginAccountRequest): LoginAccountResponse {
        val account = accountRepository.findByUsername(request.username) ?: TODO()
        if (!authPassword.matchPassword(account.id, request.password)) {
            TODO()
        }
        val session = sessionsRepository.save(AccountSessionEntity(account = account))
        val refreshToken = refreshTokensRepository.findByAccount(account) ?: refreshTokensRepository.save(
            AccountRefreshTokenEntity(account = account)
        )

        return LoginAccountResponse(
            tokens = TokensData(
                accessToken = tokenService.createAccessToken(
                    accountId = account.id,
                    sessionId = session.id,
                    authorities = authorizationService.fullAccessAuthorities().map { it.authority }
                ),
                refreshToken = tokenService.createRefreshToken(
                    accountId = account.id,
                    tokenId = refreshToken.id
                )
            ),
        )
    }

    @DelicateAuthorizationApi
    @Transactional
    @PostMapping("/register")
    override fun register(@RequestBody request: RegisterAccountRequest): RegisterAccountResponse {
        val account = accountRepository.save(AccountEntity(username = request.username))
        accountDataRepository.save(
            AccountDataEntity(
                name = request.name,
                account = account,
            )
        )
        val session = sessionsRepository.save(AccountSessionEntity(account = account))
        val refreshTokenEntity = refreshTokensRepository.save(AccountRefreshTokenEntity(account = account))
        authPassword.setPassword(accountId = account.id, rawPassword = request.password)
        if (!authPassword.matchPassword(account.id, request.password)) {
            TODO()
        }
        return RegisterAccountResponse(
            tokens = TokensData(
                accessToken = tokenService.createAccessToken(
                    accountId = account.id,
                    sessionId = session.id,
                    authorities = authorizationService.fullAccessAuthorities().map{it.authority}
                ),
                refreshToken = tokenService.createRefreshToken(
                    accountId = account.id,
                    tokenId = refreshTokenEntity.id
                )
            ),
        )
    }

}
