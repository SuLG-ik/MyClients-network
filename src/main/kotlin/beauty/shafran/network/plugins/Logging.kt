package beauty.shafran.network.plugins

import io.ktor.server.application.*
import io.ktor.server.plugins.*
import io.ktor.server.request.*
import org.slf4j.event.Level

fun Application.configureLogging() {
    install(CallLogging) {
        level = Level.INFO
        filter { call -> call.request.path().startsWith("/") }
    }
}