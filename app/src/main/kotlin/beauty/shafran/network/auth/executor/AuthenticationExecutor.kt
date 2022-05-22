package beauty.shafran.network.auth.executor

import beauty.shafran.network.auth.data.LoginAccountRequest
import beauty.shafran.network.auth.data.LoginAccountResponse
import beauty.shafran.network.auth.data.RefreshTokenRequest
import beauty.shafran.network.auth.data.RefreshTokenResponse

interface AuthenticationExecutor {

    suspend fun loginAccount(request: LoginAccountRequest): LoginAccountResponse

    suspend fun refreshAccount(request: RefreshTokenRequest): RefreshTokenResponse

}