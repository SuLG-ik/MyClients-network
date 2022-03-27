package beauty.shafran.network.plugins

import beauty.shafran.network.customers.exceptions.customersMapper
import io.ktor.application.*
import io.ktor.features.*

fun Application.configureHandler() {
    install(StatusPages) {
        customersMapper()
    }
}
