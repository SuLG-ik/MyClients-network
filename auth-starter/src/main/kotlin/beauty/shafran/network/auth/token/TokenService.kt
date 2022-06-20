package beauty.shafran.network.auth.token

import beauty.shafran.network.auth.Authority
import beauty.shafran.network.auth.AuthorizationService
import beauty.shafran.network.auth.data.AccessTokenData
import beauty.shafran.network.auth.data.RefreshTokenData
import beauty.shafran.network.auth.jwt.JWTAuthenticationConfig
import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.interfaces.DecodedJWT
import com.auth0.jwt.interfaces.JWTVerifier
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Service
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZoneOffset
import java.util.*

@Service
internal class TokenService(
    private val algorithm: Algorithm,
    private val verifier: JWTVerifier,
    @Qualifier("expired_access_token")
    private val foreverVerifier: JWTVerifier,
    private val service: AuthorizationService,
    private val config: JWTAuthenticationConfig,
) {

    fun createAccessToken(accountId: Long, sessionId: Long, authorities: List<String>): AccessTokenData {
        val expiresAt =
            LocalDateTime.ofInstant(
                Instant.ofEpochMilli(System.currentTimeMillis() + config.accessTokenExpiresAfter),
                ZoneId.systemDefault(),
            )
        return AccessTokenData(
            accessToken = signAccessToken(accountId, sessionId, authorities, expiresAt),
            sessionId = sessionId.toString(),
            accountId = accountId.toString(),
            expiresAt = expiresAt,
        )
    }

    fun createRefreshToken(accountId: Long, tokenId: Long): RefreshTokenData {
        return RefreshTokenData(
            refreshToken = signRefreshToken(accountId, tokenId),
            tokenId = tokenId.toString(),
            accountId = accountId.toString(),
        )
    }


    private fun signAccessToken(
        userId: Long,
        sessionId: Long,
        authorities: List<String>,
        expiresAt: LocalDateTime,
    ): String {
        return JWT.create()
            .withClaim("tokenType", "access")
            .withClaim("userId", userId)
            .withClaim("sessionId", sessionId)
            .withArrayClaim("authorities", authorities.toTypedArray())
            .withExpiresAt(Date.from(expiresAt.toInstant(ZoneOffset.UTC)))
            .sign(algorithm)
    }

    private fun signRefreshToken(userId: Long, tokenId: Long): String {
        return JWT.create()
            .withClaim("tokenType", "refresh")
            .withClaim("userId", userId)
            .withClaim("tokenId", tokenId)
            .withArrayClaim("authorities", service.refreshTokenAuthorities().map { it.toString() }.toTypedArray())
            .sign(algorithm)
    }

    fun decodeAbstractToken(token: String): JWTToken {
        val jwt = verifier.verify(token)
        return when (jwt.getClaim("tokenType").asString()) {
            "access" -> {
                decodeAccessToken(jwt)
            }
            "refresh" -> {
                decodeRefreshToken(jwt)
            }
            else -> {
                throw IllegalArgumentException("Token does not match any type (access or refresh)")
            }
        }
    }

    private fun decodeAccessToken(token: DecodedJWT): AccessToken {
        return AccessToken(
            token = token.token,
            userId = token.getClaim("userId").asLong(),
            sessionId = token.getClaim("sessionId").asLong(),
            authority = token.getClaim("authorities").asList(String::class.java)
        )
    }

    fun decodeExpiredAccessToken(accessToken: String): AccessToken {
        val token = foreverVerifier.verify(accessToken)
        return AccessToken(
            token = accessToken,
            userId = token.getClaim("userId").asLong(),
            sessionId = token.getClaim("sessionId").asLong(),
            authority = token.getClaim("authorities").asList(String::class.java)
        )
    }

    private fun decodeRefreshToken(token: DecodedJWT): RefreshToken {
        return RefreshToken(
            token = token.token,
            userId = token.getClaim("userId").asLong(),
            tokenId = token.getClaim("tokenId").asLong(),
            authority = token.getClaim("authorities").asList(String::class.java)
        )
    }

}

sealed class JWTToken(
    val token: String,
    val accountId: Long,
    val authority: List<String>,
)

class AccessToken(
    token: String,
    userId: Long,
    val sessionId: Long,
    authority: List<String>,
) : JWTToken(token, userId, authority)

class RefreshToken(
    token: String,
    userId: Long,
    val tokenId: Long,
    authority: List<String>,
) : JWTToken(token, userId, authority)