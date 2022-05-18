package beauty.shafran.network.admin

import beauty.shafran.get
import beauty.shafran.network.admin.account.adminAccountsRoute
import beauty.shafran.network.admin.company.adminCompanyRoute
import beauty.shafran.network.admin.permissions.adminPermissionsRoute
import io.ktor.server.routing.*

fun Route.adminRoute() {
    route("/company") { adminCompanyRoute() }
    route("/account") { adminAccountsRoute(get()) }
    route("/permission") { adminPermissionsRoute() }
}