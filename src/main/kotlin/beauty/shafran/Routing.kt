package beauty.shafran

import beauty.shafran.network.account.accountsRoute
import beauty.shafran.network.auth.authsRoute
import beauty.shafran.network.companies.companiesRoute
import beauty.shafran.network.customers.customersRoute
import beauty.shafran.network.employees.employeesRoute
import beauty.shafran.network.services.servicesRoute
import beauty.shafran.network.session.sessionsRoute
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.routing.*

fun Application.v1Routing() {
    routing {
        route("/v1") {
            authenticate(CompaniesAuthScope) {
                route("/sessions") { sessionsRoute(get()) }
                route("/services") { servicesRoute(get()) }
                route("/employees") { employeesRoute(get()) }
                route("/customers") { customersRoute(get()) }
                route("/companies") { companiesRoute(get()) }
                route("/accounts") { accountsRoute(get()) }
            }
            route("/auth") { authsRoute(get()) }
        }
    }
}