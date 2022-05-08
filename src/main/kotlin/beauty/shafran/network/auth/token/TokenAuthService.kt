package beauty.shafran.network.auth.token

import beauty.shafran.network.auth.data.AccessTokenData
import beauty.shafran.network.auth.data.RefreshTokenData
import beauty.shafran.network.auth.data.TokenData
import beauty.shafran.network.auth.entity.AccountSessionEntity

interface TokenAuthService {

    suspend fun generateTokenForSession(session: AccountSessionEntity): TokenData

    suspend fun extractSessionId(token: AccessTokenData) : String

    suspend fun extractSessionId(token: RefreshTokenData) : String

}
