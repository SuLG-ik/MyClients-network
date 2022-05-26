package beauty.shafran

import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.plugins.contentnegotiation.*
import kotlinx.serialization.json.Json
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

fun Application.serialization() {
    install(ContentNegotiation) {
        json(this@serialization.get())
    }
}

val SerializationModule = module {
    factoryOf(::json)
}

private fun json() = Json {
    classDiscriminator = "\$type"
    ignoreUnknownKeys = true
    prettyPrint = true
    encodeDefaults = true
}