package beauty.shafran.network

import beauty.shafran.network.accounts.AccountsModule
import beauty.shafran.network.auth.AuthModule
import beauty.shafran.network.companies.CompaniesModule
import beauty.shafran.network.datetime.DatetimeModule
import beauty.shafran.network.exposed.ExposedModule
import beauty.shafran.network.serialization.SerializationModule
import io.ktor.server.application.*

fun main(args: Array<String>) = io.ktor.server.netty.EngineMain.main(args)

fun Application.koin() {
    install(KoinPlugin) {
        ktorApplication(this@koin)
        modules(
            DatetimeModule,
            SerializationModule,
            ExposedModule,
            AuthModule,
            AccountsModule,
            CompaniesModule
        )
    }
}