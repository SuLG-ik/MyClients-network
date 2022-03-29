package beauty.shafran.network.plugins

import beauty.shafran.network.api.ApiPrincipal
import beauty.shafran.network.api.apiKey
import beauty.shafran.network.config.SecureConfiguration
import get
import io.ktor.server.application.*
import io.ktor.server.auth.*

fun Application.installApiKey() {
    val config = get<SecureConfiguration>()
    authentication {
        apiKey(config.keyName) {
            validate {
                if (it == config.apiKey) ApiPrincipal() else null
            }
        }
    }
}
