package beauty.shafran.network.accounts.route

import beauty.shafran.network.auth.Authorized
import beauty.shafran.network.auth.callWrapper
import beauty.shafran.network.auth.invoke
import beauty.shafran.network.get
import io.ktor.server.routing.*

fun Route.accountsRoute() {
    val authorized: Authorized = get()
    val router: AccountsRouter = get()
    authorized {
        get("/getAccount", callWrapper(router::getAccount))
    }
}


