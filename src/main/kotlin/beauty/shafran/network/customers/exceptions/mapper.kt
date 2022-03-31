package beauty.shafran.network.customers.exceptions

import beauty.shafran.network.ShafranNetworkException
import io.ktor.server.plugins.*
import io.ktor.server.response.*

fun StatusPagesConfig.networkMapper() {
    exception<ShafranNetworkException> { call, cause ->
        call.respond(cause.httpStatusCode, message = cause)
    }
}