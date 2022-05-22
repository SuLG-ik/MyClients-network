package beauty.shafran.network.auth.data

import beauty.shafran.network.account.data.AccountId
import beauty.shafran.network.auth.entity.AccountSessionId
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
    val sessionId: AccountSessionId,
    val accountId: AccountId,
) : TokenData()