package beauty.shafran.network.accounts.route

import beauty.shafran.network.auth.AuthorizedAccount
import beauty.shafran.network.accounts.data.AccountId
import beauty.shafran.network.accounts.data.GetAccountRequest
import beauty.shafran.network.accounts.data.GetAccountResponse

interface AccountsRouter {

    suspend fun getAccount(request: GetAccountRequest, account: AuthorizedAccount): GetAccountResponse

}