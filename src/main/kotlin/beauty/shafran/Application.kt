package beauty.shafran

import beauty.shafran.network.di.configureKoin
import beauty.shafran.network.plugins.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*

fun main() {
    embeddedServer(Netty, port = 8080, host = "0.0.0.0") {
        configureKoin()
        installApiKey()
        configureLogging()
        configureSerialization()
        configureRouting()
        configureHandler()
    }.start(wait = true)
}
