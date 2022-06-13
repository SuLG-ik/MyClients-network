package beauty.shafran.network.auth.route

import beauty.shafran.network.accounts.data.*
import beauty.shafran.network.auth.AuthorizedAccount
import beauty.shafran.network.auth.data.*

interface AuthRouter {

    suspend fun refresh(request: RefreshTokenRequest, account: AuthorizedAccount): RefreshTokenResponse
    suspend fun login(request: LoginAccountRequest): LoginAccountResponse
    suspend fun register(request: RegisterAccountRequest): RegisterAccountResponse

}