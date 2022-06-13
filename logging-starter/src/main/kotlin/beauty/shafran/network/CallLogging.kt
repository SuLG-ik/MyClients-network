package beauty.shafran.network

import io.ktor.server.application.*
import io.ktor.server.plugins.callloging.*
import ru.sulgik.config.ConfigurationProperties
import ru.sulgik.config.PropertySuffix


@ConfigurationProperties("ktor.deployment.callLogging")
data class CallLoggingConfig(
    @PropertySuffix("isEnabled")
    val isEnabled: Boolean,
)

fun Application.callLogging() {
    val config = environment.config.toCallLoggingConfig()
    if (config.isEnabled) {
        install(CallLogging)
    }
}