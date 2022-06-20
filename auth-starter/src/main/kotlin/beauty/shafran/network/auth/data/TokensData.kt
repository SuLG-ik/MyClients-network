package beauty.shafran.network.auth.data

import java.time.LocalDateTime


data class AccessTokenData(
    val accessToken: String,
    val sessionId: String,
    val accountId: String,
    val expiresAt: LocalDateTime,
)


data class RefreshTokenData(
    val refreshToken: String,
    val tokenId: String,
    val accountId: String,
)

class TokensData(
    val accessToken: AccessTokenData,
    val refreshToken: RefreshTokenData,
)
