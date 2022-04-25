package beauty.shafran.network.auth.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
sealed class AccessTokenData

@Serializable
@SerialName("jwt")
data class JwtAccessTokenData(val accessToken: String) : AccessTokenData()

@Serializable
sealed class RefreshTokenData

@Serializable
@SerialName("jwt")
data class JwtRefreshTokenData(val refreshToken: String) : RefreshTokenData()

@Serializable
sealed class TokenData

@Serializable
@SerialName("jwt")
class JwtTokenData(
    val accessToken: JwtAccessTokenData,
    val refreshToken: JwtRefreshTokenData,
    val expiresAt: Long,
    val sessionId: String,
    val accountId: String,
) : TokenData()