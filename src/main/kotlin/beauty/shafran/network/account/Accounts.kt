package beauty.shafran.network.account

import beauty.shafran.network.account.executors.AccountsExecutor
import beauty.shafran.network.utils.callWrapper
import io.ktor.server.routing.*

fun Route.accountsRoute(
    executor: AccountsExecutor,
) {
    get("/getAccount", callWrapper(executor::getAccount))
}