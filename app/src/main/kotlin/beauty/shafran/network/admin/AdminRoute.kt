package beauty.shafran.network.admin

import beauty.shafran.AdminAuthScope
import beauty.shafran.get
import beauty.shafran.network.admin.account.adminAccountsRoute
import beauty.shafran.network.admin.company.adminCompanyRoute
import beauty.shafran.network.admin.permissions.adminPermissionsRoute
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.routing.*

fun Application.adminRouting() {
    routing {
        authenticate(AdminAuthScope) {
            route("/v1/admin") {
                route("/company") { adminCompanyRoute(get()) }
                route("/account") { adminAccountsRoute(get()) }
                route("/permission") { adminPermissionsRoute() }
            }
        }
    }
}