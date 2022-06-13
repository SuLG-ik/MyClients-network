package beauty.shafran.network.auth.token

import beauty.shafran.network.accounts.data.*
import beauty.shafran.network.accounts.data.AccountId
import beauty.shafran.network.accounts.data.AccountSessionId
import beauty.shafran.network.auth.data.*
import beauty.shafran.network.auth.jwt.JWTAuthenticationConfig
import beauty.shafran.network.auth.tables.*
import beauty.shafran.network.database.TransactionalScope
import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toJavaInstant
import kotlinx.datetime.toLocalDateTime
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.select
import java.util.*
import kotlin.time.DurationUnit
import kotlin.time.toDuration

internal class AuthTokenServiceImpl(
    private val algorithm: Algorithm,
    private val clock: Clock,
    private val config: JWTAuthenticationConfig,
    private val timeZone: TimeZone,
) : AuthTokenService {

    context(TransactionalScope) override suspend fun generateTokens(
        accountId: AccountId,
        authorities: List<String>,
    ): TokensData {
        val session = AccountSessionTable.insertAndGetEntity(accountId = accountId.value)
        val refreshTokenEntity =
            AccountRefreshTokenTable.select { AccountRefreshTokenTable.accountId eq accountId.value }
                .firstOrNull()?.toAccountRefreshTokenEntity()
                ?: return generateNewTokens(sessionId = AccountSessionId(session.id), accountId, authorities)
        val accessToken =
            generateAccessToken(
                sessionId = AccountSessionId(session.id),
                accountId = accountId,
                authorities = authorities
            )
        val refreshToken = generateRefreshToken(accountId = accountId, tokenId = TokenId(refreshTokenEntity.id))
        return JwtTokensData(
            accessToken = accessToken,
            refreshToken = refreshToken,
            sessionId = AccountSessionId(session.id),
            accountId = accountId
        )
    }


    context(TransactionalScope) override suspend fun invalidateAndGenerateTokens(
        accountId: AccountId,
        authorities: List<String>,
    ): TokensData {
        invalidateTokens(accountId)
        val session = AccountSessionTable.insertAndGetEntity(accountId = accountId.value)
        return generateNewTokens(sessionId = AccountSessionId(session.id), accountId, authorities)
    }

    context (TransactionalScope) private suspend fun generateNewTokens(
        sessionId: AccountSessionId,
        accountId: AccountId,
        authorities: List<String>,
    ): JwtTokensData {
        val refreshTokenEntity = AccountRefreshTokenTable.insertAndGetEntity(accountId = accountId.value)
        val accessToken =
            generateAccessToken(
                sessionId = sessionId,
                accountId = accountId,
                authorities = authorities
            )
        val refreshToken = generateRefreshToken(accountId = accountId, tokenId = TokenId(refreshTokenEntity.id))
        return JwtTokensData(
            accessToken = accessToken,
            refreshToken = refreshToken,
            sessionId = sessionId,
            accountId = accountId
        )
    }

    context(TransactionalScope) private suspend fun invalidateTokens(accountId: AccountId) {
        AccountSessionTable.deleteWhere { AccountSessionTable.accountId eq accountId.value }
        AccountRefreshTokenTable.deleteWhere { AccountSessionTable.accountId eq accountId.value }
    }

    context(TransactionalScope) override suspend fun refreshTokens(
        tokenId: TokenId,
        accountId: AccountId,
        sessionId: AccountSessionId,
        authorities: List<String>,
    ): TokensData {
        val accessToken = generateAccessToken(
            sessionId = sessionId,
            accountId = accountId,
            authorities = authorities
        )
        val refreshToken = generateRefreshToken(
            accountId = accountId, tokenId = tokenId
        )
        return JwtTokensData(
            accessToken = accessToken,
            refreshToken = refreshToken,
            sessionId = sessionId,
            accountId = accountId
        )
    }

    context(TransactionalScope) override suspend fun isSessionAuthorized(sessionId: AccountSessionId): Boolean {
        return AccountSessionTable.findById(sessionId.value) != null
    }

    context(TransactionalScope) override suspend fun isRefreshTokenAuthorized(id: TokenId): Boolean {
        return AccountRefreshTokenTable.findById(id.value) != null
    }

    context(TransactionalScope) private fun generateAccessToken(
        sessionId: AccountSessionId,
        accountId: AccountId,
        authorities: List<String>,
    ): JwtAccessTokenData {
        val expiresAt = clock.now().plus(config.accessTokenExpiresAfter.toDuration(DurationUnit.MILLISECONDS))
        val token = JWT.create()
            .withClaim("accountId", accountId.value)
            .withClaim("sessionId", sessionId.value)
            .withArrayClaim("authorities", authorities.toTypedArray())
            .withExpiresAt(Date.from(expiresAt.toJavaInstant()))
            .withAudience(config.audience)
            .sign(algorithm)
        return JwtAccessTokenData(token, expiresAt.toLocalDateTime(timeZone))
    }

    context(TransactionalScope) private fun generateRefreshToken(
        accountId: AccountId,
        tokenId: TokenId,
    ): JwtRefreshTokenData {
        val token = JWT.create()
            .withClaim("accountId", accountId.value)
            .withJWTId(tokenId.value.toString())
            .withArrayClaim("authorities", arrayOf("SCOPE_refresh"))
            .withAudience(config.audience)
            .sign(algorithm)
        return JwtRefreshTokenData(token, tokenId)
    }


}