package beauty.shafran.network.auth.token

import beauty.shafran.network.accounts.data.AccountId
import beauty.shafran.network.accounts.data.AccountSessionId
import beauty.shafran.network.auth.data.TokenId
import beauty.shafran.network.auth.data.TokensData
import beauty.shafran.network.auth.data.*
import beauty.shafran.network.database.TransactionalScope

interface AuthTokenService {

    context(TransactionalScope) suspend fun isSessionAuthorized(sessionId: AccountSessionId): Boolean

    context(TransactionalScope) suspend fun isRefreshTokenAuthorized(id: TokenId): Boolean

    context(TransactionalScope) suspend fun invalidateAndGenerateTokens(
        accountId: AccountId,
        authorities: List<String>
    ): TokensData

    context(TransactionalScope) suspend fun refreshTokens(
        tokenId: TokenId,
        accountId: AccountId,
        sessionId: AccountSessionId,
        authorities: List<String>
    ): TokensData

    context(TransactionalScope) suspend fun generateTokens(accountId: AccountId, authorities: List<String>): TokensData
}