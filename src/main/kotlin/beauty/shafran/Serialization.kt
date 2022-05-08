package beauty.shafran

import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.plugins.contentnegotiation.*
import kotlinx.serialization.json.Json
import org.koin.core.annotation.Module
import org.koin.core.annotation.Single

fun Application.serialization() {
    install(ContentNegotiation) {
        json(this@serialization.get())
    }
}

@Module
class Serialization {

    @Single
    fun json() = Json {
        classDiscriminator = "\$type"
        ignoreUnknownKeys = true
        prettyPrint = true
        encodeDefaults = true
    }

}