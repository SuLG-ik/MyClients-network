package beauty.shafran.network.auth.route

import beauty.shafran.network.auth.AuthorizedAuthentication
import beauty.shafran.network.auth.RefreshAuthorizedAccount
import beauty.shafran.network.auth.RefreshAuthorizedAuthentication
import beauty.shafran.network.auth.data.*

interface AuthRouter {
    fun login(request: LoginAccountRequest): LoginAccountResponse
    fun register(request: RegisterAccountRequest): RegisterAccountResponse

    fun refresh(request: RefreshTokenRequest, account: RefreshAuthorizedAccount): RefreshTokenResponse
}