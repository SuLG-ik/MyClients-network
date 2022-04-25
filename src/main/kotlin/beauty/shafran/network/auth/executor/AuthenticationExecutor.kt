package beauty.shafran.network.auth.executor

import beauty.shafran.network.auth.data.*

interface AuthenticationExecutor {

    suspend fun registerAccount(request: RegisterAccountRequest): RegisterAccountResponse

    suspend fun loginAccount(request: LoginAccountRequest): LoginAccountResponse

    suspend fun refreshAccount(request: RefreshTokenRequest): RefreshTokenResponse

}