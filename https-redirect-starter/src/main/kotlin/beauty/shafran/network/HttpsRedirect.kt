package beauty.shafran.network

import io.ktor.server.application.*
import io.ktor.server.plugins.httpsredirect.*
import ru.sulgik.config.ConfigurationProperties
import ru.sulgik.config.PropertySuffix


@ConfigurationProperties("ktor.deployment.httpsRedirect")
data class HttpsRedirectConfig(
    @PropertySuffix("isEnabled")
    val isEnabled: Boolean,
    @PropertySuffix("sslPort")
    val sslPort: Int,
)

fun Application.httpsRedirect() {
    val config = environment.config.toHttpsRedirectConfig()
    if (config.isEnabled)
        install(HttpsRedirect) {
            sslPort = config.sslPort
        }
}