package beauty.shafran.network.admin.account.executor

import beauty.shafran.network.auth.data.RegisterAccountRequest
import beauty.shafran.network.auth.data.RegisterAccountResponse

interface AdminAccountExecutor {

    suspend fun registerAccount(request: RegisterAccountRequest): RegisterAccountResponse

}