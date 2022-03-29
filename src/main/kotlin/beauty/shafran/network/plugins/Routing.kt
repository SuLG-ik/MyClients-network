package beauty.shafran.network.plugins

import beauty.shafran.network.customers.configureCustomers
import beauty.shafran.network.employees.configureEmployees
import beauty.shafran.network.services.configureServices
import beauty.shafran.network.session.configureSessions
import io.ktor.server.application.*
import io.ktor.server.routing.*

fun Application.configureRouting() {
    // Starting point for a Ktor app:
    routing() {
        route("/v1") {
            configureServices()
            configureCustomers()
            configureSessions()
            configureEmployees()
        }
    }
}