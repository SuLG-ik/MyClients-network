package beauty.shafran

import beauty.shafran.network.di.configureKoin
import beauty.shafran.network.plugins.configureHandler
import beauty.shafran.network.plugins.configureRouting
import beauty.shafran.network.plugins.configureSerialization
import io.ktor.server.engine.*
import io.ktor.server.netty.*

fun main() {
    embeddedServer(Netty, port = 8080, host = "0.0.0.0") {
        configureKoin()
        configureSerialization()
        configureRouting()
        configureHandler()
    }.start(wait = true)
}
