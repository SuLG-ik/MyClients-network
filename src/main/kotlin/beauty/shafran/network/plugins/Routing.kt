package beauty.shafran.network.plugins

import beauty.shafran.network.customers.configureCustomers
import beauty.shafran.network.employees.configureEmployees
import beauty.shafran.network.services.configureServices
import beauty.shafran.network.session.configureSessions
import io.ktor.application.*
import io.ktor.routing.*

fun Application.configureRouting() {
    // Starting point for a Ktor app:
    routing() {
        configureServices()
        configureCustomers()
        configureSessions()
        configureEmployees()
    }
}
