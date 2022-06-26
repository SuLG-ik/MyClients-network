package beauty.shafran.network.auth

import beauty.shafran.network.accounts.entities.AccountDataEntity
import beauty.shafran.network.accounts.entities.AccountEntity
import beauty.shafran.network.accounts.repositories.AccountDataEntityRepository
import beauty.shafran.network.accounts.repositories.AccountRepository
import beauty.shafran.network.auth.entities.AccountRefreshTokenEntity
import beauty.shafran.network.auth.entities.AccountSessionEntity
import beauty.shafran.network.auth.jwt.JWTAuthenticationConfig
import beauty.shafran.network.auth.repository.AccountRefreshTokenRepository
import beauty.shafran.network.auth.repository.AccountSessionsRepository
import beauty.shafran.network.auth.repository.AuthPasswordRepository
import beauty.shafran.network.auth.route.TokensData
import beauty.shafran.network.auth.token.TokenService
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId

@Service
internal class AuthenticationService(
    private val accountRepository: AccountRepository,
    private val accountDataRepository: AccountDataEntityRepository,
    private val authPassword: AuthPasswordRepository,
    private val tokenService: TokenService,
    private val sessionsRepository: AccountSessionsRepository,
    private val refreshTokensRepository: AccountRefreshTokenRepository,
    private val authorizationService: AuthorizationService,
    private val config: JWTAuthenticationConfig,
) {

    @Transactional
    fun refresh(
        accessToken: String,
        accountId: Long,
        tokenId: Long,
    ): TokensData {
        val token = tokenService.decodeExpiredAccessToken(accessToken)
        if (token.accountId != accountId)
            TODO()
        val session = sessionsRepository.findByIdOrNull(token.sessionId)
        if (session?.isDeactivated != false)
            TODO()
        return TokensData(
            accountId = accountId,
            sessionId = session.id,
            tokenId = tokenId,
            expiresAt = LocalDateTime.ofInstant(
                Instant.ofEpochMilli(System.currentTimeMillis() + config.accessTokenExpiresAfter),
                ZoneId.systemDefault(),
            ),
            authorities = token.authority,
        )
    }

    @Transactional
    @DelicateAuthorizationApi
    fun login(username: String, password: String): TokensData {
        val account = accountRepository.findByUsername(username) ?: TODO()
        if (!authPassword.matchPassword(account.id, password)) {
            TODO()
        }
        val session = sessionsRepository.save(AccountSessionEntity(account = account))
        val refreshToken = refreshTokensRepository.findByAccount(account) ?: refreshTokensRepository.save(
            AccountRefreshTokenEntity(account = account)
        )
        return TokensData(
            accountId = account.id,
            sessionId = session.id,
            tokenId = refreshToken.id,
            expiresAt = currentAccessTokenExpiresAt(),
            authorities = authorizationService.fullAccessAuthorities().map { it.authority },
        )
    }

    @Transactional
    fun register(username: String, name: String, password: String): TokensData {
        if (accountRepository.existsByUsername(username))
            TODO()
        val account = accountRepository.save(AccountEntity(username = username))
        accountDataRepository.save(
            AccountDataEntity(
                name = name,
                account = account,
            )
        )
        authPassword.setPassword(accountId = account.id, rawPassword = password)
        val refreshTokenEntity = refreshTokensRepository.save(AccountRefreshTokenEntity(account = account))
        val session = sessionsRepository.save(AccountSessionEntity(account = account))
        return TokensData(
            accountId = account.id,
            sessionId = session.id,
            tokenId = refreshTokenEntity.id,
            expiresAt = currentAccessTokenExpiresAt(),
            authorities = authorizationService.fullAccessAuthorities().map { it.authority },
        )
    }

    private fun currentAccessTokenExpiresAt(): LocalDateTime {
        return LocalDateTime.ofInstant(
            Instant.ofEpochMilli(System.currentTimeMillis() + config.accessTokenExpiresAfter),
            ZoneId.systemDefault(),
        )
    }

}