package beauty.shafran.network

import beauty.shafran.network.auth.Authorized
import io.ktor.server.auth.*
import io.ktor.server.routing.*

internal class KtorAuthAuthorized : Authorized {

    context(Route) override fun withAccount(block: Route.() -> Unit) {
        authenticate("client") {
            block(this@authenticate)
        }
    }

}

