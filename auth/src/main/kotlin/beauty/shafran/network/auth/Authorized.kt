package beauty.shafran.network.auth


import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.util.pipeline.*

interface Authorized {

    context (Route)
    fun withAccount(block: Route.() -> Unit)

}

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
        call.respond(action(call.receive(), call.principal<AuthorizedAccountPrincipal>()!!.account))
    }
}

context (Route) operator fun Authorized.invoke(block: Route.() -> Unit) {
    return withAccount(block)
}
