package beauty.shafran.network.customers.exceptions

import io.ktor.application.*
import io.ktor.features.*
import io.ktor.http.*
import io.ktor.response.*

fun StatusPages.Configuration.customersMapper() {
    exception<CustomersException> { cause ->
        call.respond(HttpStatusCode(cause.httpCode, ""), cause)
    }
}