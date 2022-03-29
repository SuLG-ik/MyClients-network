package beauty.shafran.network.plugins

import beauty.shafran.network.customers.exceptions.customersMapper
import io.ktor.server.application.*
import io.ktor.server.plugins.*

fun Application.configureHandler() {
    install(StatusPages) {
        customersMapper()
    }
}
