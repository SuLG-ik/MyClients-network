package beauty.shafran.network

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.response.*
import kotlinx.serialization.SerializationException

fun Application.exceptionsHandler() {
    install(StatusPages) {
        exception { call: ApplicationCall, _: SerializationException ->
            call.tryRespondException(BadRequest())
        }
        exception { call: ApplicationCall, cause: NetworkException ->
            call.tryRespondException(cause)
        }
    }
}

private suspend fun ApplicationCall.tryRespondException(cause: NetworkException) {
    try {
        respond(cause.httpStatusCode, cause)
    } catch (e: Exception) {
        application.log.error("Can't send exception respond", e)
        respond(HttpStatusCode.InternalServerError)
    }
}