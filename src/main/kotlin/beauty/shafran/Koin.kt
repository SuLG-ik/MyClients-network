package beauty.shafran

import io.ktor.events.EventDefinition
import io.ktor.server.application.*
import io.ktor.server.routing.*
import org.koin.core.Koin
import org.koin.core.KoinApplication
import org.koin.core.context.GlobalContext
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.core.parameter.ParametersDefinition
import org.koin.core.qualifier.Qualifier
import org.koin.ksp.generated.*


fun Application.koin() {
    install(KoinPlugin) {
        modules(
            defaultModule,
            SerializationModule,
            KMongoCModuleModule,
            PasswordModuleModule,
            CompaniesModuleModule,
            AuthModuleModule,
            AccountsBeanModule,
            AssetsModule,
            JwtConfigurationModule,
        )
    }
}

val KoinPlugin = createApplicationPlugin("Koin", { KoinApplication.init() }) {
    val monitor = environment?.monitor
    monitor?.subscribe(ApplicationStopped) {
        monitor.raise(KoinApplicationStopPreparing, pluginConfig)
        stopKoin()
        monitor.raise(KoinApplicationStopped, pluginConfig)
    }

    startKoin(pluginConfig)
    monitor?.raise(KoinApplicationStarted, pluginConfig)
}


val KoinApplicationStarted = EventDefinition<KoinApplication>()
val KoinApplicationStopPreparing = EventDefinition<KoinApplication>()
val KoinApplicationStopped = EventDefinition<KoinApplication>()

fun Application.getKoin(): Koin = GlobalContext.get()

inline fun <reified T : Any> Application.inject(
    qualifier: Qualifier? = null,
    noinline parameters: ParametersDefinition? = null,
) =
    lazy { get<T>(qualifier, parameters) }

inline fun <reified T : Any> Application.get(
    qualifier: Qualifier? = null,
    noinline parameters: ParametersDefinition? = null,
) =
    getKoin().get<T>(qualifier, parameters)

fun <T : Any> Application.getProperty(key: String) =
    getKoin().getProperty<T>(key)

fun Application.getProperty(key: String, defaultValue: String) =
    getKoin().getProperty(key) ?: defaultValue

inline fun <reified T : Any> Routing.inject(
    qualifier: Qualifier? = null,
    noinline parameters: ParametersDefinition? = null,
) =
    lazy { get<T>(qualifier, parameters) }

inline fun <reified T : Any> Routing.get(
    qualifier: Qualifier? = null,
    noinline parameters: ParametersDefinition? = null,
) =
    getKoin().get<T>(qualifier, parameters)

fun <T : Any> Routing.getProperty(key: String) =
    getKoin().getProperty<T>(key)

inline fun <reified T> Routing.getProperty(key: String, defaultValue: T) =
    getKoin().getProperty(key) ?: defaultValue

fun Routing.getKoin() = GlobalContext.get()

inline fun <reified T : Any> Route.inject(
    qualifier: Qualifier? = null,
    noinline parameters: ParametersDefinition? = null,
) =
    lazy { get<T>(qualifier, parameters) }

inline fun <reified T : Any> Route.get(
    qualifier: Qualifier? = null,
    noinline parameters: ParametersDefinition? = null,
) =
    getKoin().get<T>(qualifier, parameters)

fun <T : Any> Route.getProperty(key: String) =
    getKoin().getProperty<T>(key)

fun Route.getProperty(key: String, defaultValue: String) =
    getKoin().getProperty(key) ?: defaultValue

fun Route.getKoin() = GlobalContext.get()