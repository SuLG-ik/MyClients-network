package beauty.shafran.network

import beauty.shafran.network.accounts.route.accountsRoute
import beauty.shafran.network.auth.route.authRoute
import beauty.shafran.network.companies.route.companiesRoute
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import ru.sulgik.config.ConfigurationProperties
import ru.sulgik.config.PropertySuffix


@ConfigurationProperties("ktor.deployment.routing.v1")
data class RoutingV1Config(
    @PropertySuffix("isEnabled")
    val isEnabled: Boolean,
    @PropertySuffix("entrypoint")
    val baseEntrypoint: String,
)

fun Application.routing() {
    val config = environment.config.toRoutingV1Config()

    if (config.isEnabled)
        routing {
            route(config.baseEntrypoint) {
                route("/accounts") { accountsRoute() }
                route("/companies") { companiesRoute() }
                route("/auth") { authRoute() }
            }
        }

}