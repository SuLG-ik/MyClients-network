package beauty.shafran.network

import io.ktor.util.logging.*
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.Transaction
import org.jetbrains.exposed.sql.transactions.transaction
import ru.sulgik.exposed.generated.createAnnotatedMissingTablesAndColumns

class NativeDatabaseInitializer(
    private val logger: Logger,
) : DatabaseInitializer {

    override fun initialize(configuration: DatabaseConfiguration): Database {
        val database = Database.connect(url = configuration.url,
            driver = configuration.driver,
            user = configuration.user,
            password = configuration.password,
            setupConnection = {
                it.schema = "public"
            })

        transaction {
            SchemaUtils.createAnnotatedMissingTablesAndColumns()
        }
        return database
    }

}

context(Transaction) inline fun <reified T : Enum<T>> SchemaUtils.createMissingEnum(sql: String) {
    val enums = enumValues<T>().joinToString(",") { "'$it'" }
    exec("DROP TYPE IF EXISTS ${sql}; CREATE TYPE $sql AS ENUM ($enums);")
}

