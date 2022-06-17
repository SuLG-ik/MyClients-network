package beauty.shafran.network.exposed

import beauty.shafran.network.database.SetupTransactional
import beauty.shafran.network.database.Transactional
import beauty.shafran.network.database.TransactionalScope
import io.ktor.server.config.*
import kotlinx.coroutines.Deferred
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.transactions.experimental.suspendedTransactionAsync
import org.jetbrains.exposed.sql.transactions.transaction
import org.koin.core.module.dsl.createdAtStart
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.withOptions
import org.koin.dsl.bind
import org.koin.dsl.module
import ru.sulgik.config.ConfigurationProperties
import ru.sulgik.config.PropertySuffix

@ConfigurationProperties("exposed.datasource")
internal class DatabaseConfiguration(
    @PropertySuffix("url") val url: String,
    @PropertySuffix("user") val user: String,
    @PropertySuffix("password") val password: String,
    @PropertySuffix("driver") val driver: String,
    @PropertySuffix("createOnSetup") val isEnabled: Boolean,
)

val ExposedModule = module {
    factory { get<ApplicationConfig>().toDatabaseConfiguration() }
    singleOf(::buildDatabase) withOptions { createdAtStart() }
    singleOf(::ExposedTransactional) bind Transactional::class
    singleOf(::ExposedSetupTransactional) bind SetupTransactional::class
}

private fun buildDatabase(configuration: DatabaseConfiguration): Database {
    return Database.connect(url = configuration.url,
        driver = configuration.driver,
        user = configuration.user,
        password = configuration.password,
        setupConnection = {
            it.schema = "public"
        })
}

private class ExposedSetupTransactional(
    private val db: Database,
    private val config: DatabaseConfiguration,
) : SetupTransactional {
    override fun setup(statement: () -> Unit) {
        if (config.isEnabled)
            transaction(db = db) {
                statement()
            }
    }
}


private class DelegateTransactionalScope(
    private val db: Database,
) : TransactionalScope {
    override suspend fun <T> transactionAsync(statement: suspend TransactionalScope.() -> T): Deferred<T> {
        return suspendedTransactionAsync(db = db, statement = { statement() })
    }
}

private class ExposedTransactional(
    private val db: Database,
) : Transactional {

    override suspend fun <T> withSuspendedTransaction(statement: suspend TransactionalScope.() -> T): T {
        return newSuspendedTransaction(db = db, statement = {
            statement.invoke(DelegateTransactionalScope(db))
        })
    }
}