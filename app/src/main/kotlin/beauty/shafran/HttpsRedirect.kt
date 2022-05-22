package beauty.shafran

import io.ktor.server.application.*
import io.ktor.server.config.*
import io.ktor.server.plugins.httpsredirect.*

fun Application.httpsRedirect() {
    val port = environment.config.tryGetString("ktor.deployment.sslPort")?.toIntOrNull() ?: return
    install(HttpsRedirect) {
        this.permanentRedirect = true
        this.sslPort = port
    }
}