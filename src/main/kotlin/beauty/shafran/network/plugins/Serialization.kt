package beauty.shafran.network.plugins

import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.plugins.*
import kotlinx.serialization.json.Json

fun Application.configureSerialization() {
    install(ContentNegotiation) {
        json(
            Json {
                classDiscriminator = "\$type"
                ignoreUnknownKeys = true
            }
        )
    }
}
