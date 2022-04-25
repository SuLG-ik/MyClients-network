package beauty.shafran.network.auth.token

import beauty.shafran.network.auth.data.AuthorizedAccount
import beauty.shafran.network.auth.data.RefreshTokenData
import beauty.shafran.network.auth.data.TokenData
import beauty.shafran.network.auth.entity.AccountSessionEntity

interface TokenAuthService {

    suspend fun generateTokenForSession(session: AccountSessionEntity): TokenData

    suspend fun findSessionWithRefreshToken(refreshTokenData: RefreshTokenData): AccountSessionEntity

    suspend fun authorizeAccount(sessionId: String): AuthorizedAccount
}
