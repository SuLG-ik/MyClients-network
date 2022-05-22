package beauty.shafran.network.utils

import beauty.shafran.AccessDenied
import beauty.shafran.network.auth.data.AuthorizedAccount
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.util.pipeline.*

inline fun <reified Receive : Any, reified Respond : Any> callWrapper(
    crossinline action: suspend (Receive) -> Respond,
): PipelineInterceptor<Unit, ApplicationCall> {
    return {
        call.respond(action(call.receive()))
    }
}

inline fun <reified Receive : Any, reified Respond : Any> callWrapper(
    crossinline action: suspend (Receive, AuthorizedAccount) -> Respond,
): PipelineInterceptor<Unit, ApplicationCall> {
    return {
        val account = call.authentication.principal<AuthorizedAccount>() ?: throw AccessDenied()
        call.respond(action(call.receive(), account))
    }
}
