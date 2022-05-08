package beauty.shafran.network.account.executors

import beauty.shafran.network.account.data.GetAttachedAccountRequest
import beauty.shafran.network.account.data.GetAttachedAccountResponse
import beauty.shafran.network.auth.data.AuthorizedAccount

interface AccountsExecutor {

    suspend fun getAccount(request: GetAttachedAccountRequest, account: AuthorizedAccount): GetAttachedAccountResponse

}