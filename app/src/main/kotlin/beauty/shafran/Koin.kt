package beauty.shafran

import beauty.shafran.network.ClockModule
import beauty.shafran.network.ExposedBeanModule
import beauty.shafran.network.LoggerModule
import beauty.shafran.network.account.AccountsModule
import beauty.shafran.network.admin.AdminModule
import beauty.shafran.network.api.PasswordModule
import beauty.shafran.network.assets.AssetsModule
import beauty.shafran.network.auth.AuthModule
import beauty.shafran.network.auth.jwt.JwtModule
import beauty.shafran.network.cards.CardsModule
import beauty.shafran.network.companies.CompanyModule
import beauty.shafran.network.customers.CustomersModule
import beauty.shafran.network.employees.EmployeesModule
import beauty.shafran.network.phone.PhoneModule
import beauty.shafran.network.services.ServicesModule
import beauty.shafran.network.session.ServiceSessionsModule
import beauty.shafran.network.utils.RandomModule
import io.ktor.events.EventDefinition
import io.ktor.server.application.*
import io.ktor.server.routing.*
import org.koin.core.Koin
import org.koin.core.KoinApplication
import org.koin.core.context.GlobalContext
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.core.logger.Level
import org.koin.core.module.Module
import org.koin.core.parameter.ParametersDefinition
import org.koin.core.qualifier.Qualifier
import org.koin.dsl.bind
import org.koin.dsl.module

fun Application.koin() {
    install(KoinPlugin) {
        ktorApplication(this@koin)
        modules(
            AccountsModule,
            AdminModule,
            AssetsModule,
            CardsModule,
            CompanyModule,
            CustomersModule,
            EmployeesModule,
            ServicesModule,
            ServiceSessionsModule,
            AuthModule,
            ExposedBeanModule,
            SerializationModule,
            JwtModule,
            PasswordModule,
            PhoneModule,
            RandomModule,
            ClockModule,
            LoggerModule,
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

fun KoinApplication.ktorApplication(application: Application): KoinApplication {
    if (koin.logger.isAt(Level.INFO)) {
        koin.logger.info("[init] declare Ktor Application")
    }

    koin.loadModules(listOf(application.koinModules))

    return this
}

private val Application.koinModules: Module
    get() {
        return module {
            single { this@koinModules } bind Application::class
            factory { get<Application>().environment } bind ApplicationEnvironment::class
            factory { get<ApplicationEnvironment>().config }
            factory { get<Application>().log }
        }
    }

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