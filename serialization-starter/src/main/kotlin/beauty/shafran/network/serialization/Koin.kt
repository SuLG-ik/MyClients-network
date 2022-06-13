package beauty.shafran.network.serialization

import kotlinx.serialization.json.Json
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val SerializationModule = module {
    factoryOf(::json)
}

private fun json() = Json {
    classDiscriminator = "\$type"
    ignoreUnknownKeys = true
    prettyPrint = true
    encodeDefaults = true
}