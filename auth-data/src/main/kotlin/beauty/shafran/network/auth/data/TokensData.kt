package beauty.shafran.network.auth.data

import beauty.shafran.network.accounts.data.AccountId
import beauty.shafran.network.accounts.data.AccountSessionId
import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
sealed class TokensData


@Serializable
data class JwtAccessTokenData(
    val accessToken: String,
    val expiresAt: LocalDateTime,
)

@Serializable
data class JwtRefreshTokenData(
    val refreshToken: String,
    val tokenId: TokenId,
)

@Serializable
@JvmInline
value class TokenId(val value: Long)

@Serializable
@SerialName("jwt")
class JwtTokensData(
    val accessToken: JwtAccessTokenData,
    val refreshToken: JwtRefreshTokenData,
    val sessionId: AccountSessionId,
    val accountId: AccountId,
) : TokensData()
