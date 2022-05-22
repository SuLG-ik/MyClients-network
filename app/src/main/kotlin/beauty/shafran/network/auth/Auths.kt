package beauty.shafran.network.auth

import beauty.shafran.network.auth.executor.AuthenticationExecutor
import beauty.shafran.network.utils.callWrapper
import io.ktor.server.routing.*

fun Route.authsRoute(
    executor: AuthenticationExecutor,
) {
    get("/login", callWrapper(executor::loginAccount))
    get("/refresh", callWrapper(executor::refreshAccount))
}