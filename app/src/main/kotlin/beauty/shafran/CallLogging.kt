package beauty.shafran

import io.ktor.server.application.*
import io.ktor.server.config.*
import io.ktor.server.plugins.callloging.*

fun Application.callLogging() {
    if (environment.config.tryGetString("ktor.deployment.callLogging") == "true")
        install(CallLogging)
}