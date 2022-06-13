package beauty.shafran.network

import io.ktor.server.application.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.response.*
import kotlinx.serialization.SerializationException

fun Application.exceptionsHandler() {
    install(StatusPages) {
        exception { call: ApplicationCall, cause: SerializationException ->
            val exception = BadRequest()
            call.respond(exception.httpStatusCode, exception)
        }
        exception { call: ApplicationCall, cause: NetworkException ->
            this@exceptionsHandler.log.warn("Error during request", cause)
            call.respond(cause.httpStatusCode, cause)
        }
    }
}