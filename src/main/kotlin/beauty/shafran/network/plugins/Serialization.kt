package beauty.shafran.network.plugins

import beauty.shafran.network.exceptionsSerializationModule
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.plugins.*
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule

fun Application.configureSerialization() {
    install(ContentNegotiation) {
        json(
            Json {
                classDiscriminator = "\$type"
                ignoreUnknownKeys = true
                serializersModule = SerializersModule {
                    exceptionsSerializationModule()
                }
            }
        )
    }
}

