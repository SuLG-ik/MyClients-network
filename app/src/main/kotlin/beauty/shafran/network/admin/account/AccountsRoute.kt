package beauty.shafran.network.admin.account

import beauty.shafran.network.admin.account.executor.AdminAccountExecutor
import beauty.shafran.network.utils.callWrapper
import io.ktor.server.routing.*

fun Route.adminAccountsRoute(executor: AdminAccountExecutor) {
    post("/createAccount", callWrapper(executor::registerAccount))
}