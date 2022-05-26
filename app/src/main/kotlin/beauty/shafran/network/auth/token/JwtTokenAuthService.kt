package beauty.shafran.network.auth.token

import beauty.shafran.BadRequest
import beauty.shafran.IllegalToken
import beauty.shafran.TokenExpired
import beauty.shafran.network.auth.data.*
import beauty.shafran.network.auth.entity.AccountSessionEntity
import beauty.shafran.network.auth.entity.AccountSessionId
import beauty.shafran.network.auth.jwt.JwtConfig
import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.exceptions.JWTDecodeException
import com.auth0.jwt.exceptions.JWTVerificationException
import com.auth0.jwt.exceptions.TokenExpiredException
import java.util.*


class JwtTokenAuthService(
    private val jwtConfig: JwtConfig,
    private val algorithm: Algorithm,
) : TokenAuthService {

    private val verifier = JWT.require(algorithm)
        .acceptLeeway(5).build()

    override suspend fun generateTokenForSession(session: AccountSessionEntity): TokenData {
        val expiresAt = Date(System.currentTimeMillis() + jwtConfig.accessTokenExpiresAfter)
        return JwtTokenData(
            accessToken = generateJwtAccessToken(session, expiresAt),
            refreshToken = generateJwtRefreshToken(session),
            sessionId = session.id,
            expiresAt = expiresAt.time,
            accountId = session.accountId
        )
    }

    private fun extractSessionId(token: String): AccountSessionId {
        return try {
            val id = verifier.verify(token).getClaim("sessionId").asLong() ?: throw BadRequest()
            AccountSessionId(id)
        } catch (e: TokenExpiredException) {
            throw TokenExpired()
        } catch (e: JWTVerificationException) {
            throw IllegalToken()
        } catch (e: JWTDecodeException) {
            throw IllegalToken()
        }
    }

    override suspend fun extractSessionId(token: AccessTokenData): AccountSessionId {
        when (token) {
            is JwtAccessTokenData -> {
                return extractSessionId(token.accessToken)
            }
        }
    }

    override suspend fun extractSessionId(token: RefreshTokenData): AccountSessionId {
        when (token) {
            is JwtRefreshTokenData -> {
                return extractSessionId(token.refreshToken)
            }
        }

    }

    private fun generateJwtAccessToken(session: AccountSessionEntity, expiresIn: Date): JwtAccessTokenData {
        val jwt = JWT.create()
            .withIssuer(jwtConfig.issuer)
            .withExpiresAt(expiresIn)
            .withClaim("sessionId", session.id.id)
            .withIssuedAt(Date())
            .withAudience(jwtConfig.audience)
            .sign(algorithm)
        return JwtAccessTokenData(jwt)
    }

    private fun generateJwtRefreshToken(session: AccountSessionEntity): JwtRefreshTokenData {
        val jwt = JWT.create()
            .withIssuer(jwtConfig.issuer)
            .withExpiresAt(Date(System.currentTimeMillis() + jwtConfig.refreshTokenExpiresAfter))
            .withClaim("sessionId", session.id.id)
            .withIssuedAt(Date())
            .withAudience(jwtConfig.audience)
            .sign(algorithm)
        return JwtRefreshTokenData(jwt)
    }


}