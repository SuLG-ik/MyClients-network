package beauty.shafran.network.customers.exceptions

import io.ktor.http.*
import io.ktor.server.plugins.*
import io.ktor.server.response.*

fun StatusPagesConfig.customersMapper() {
    exception<CustomersException> { call, cause ->
        call.respond(HttpStatusCode(cause.httpCode, ""), cause)
    }
}