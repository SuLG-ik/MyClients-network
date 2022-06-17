package beauty.shafran.network.accounts.route

import beauty.shafran.network.accounts.schema.createAnnotatedMissingTablesAndColumns
import beauty.shafran.network.accounts.schema.createAnnotatedTablesAndColumns
import beauty.shafran.network.auth.Authorized
import beauty.shafran.network.auth.callWrapper
import beauty.shafran.network.auth.invoke
import beauty.shafran.network.database.SetupTransactional
import beauty.shafran.network.get
import io.ktor.server.routing.*
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction

fun Route.accountsRoute() {
    get<SetupTransactional>().setup {
        SchemaUtils.createAnnotatedMissingTablesAndColumns()
    }
    val authorized: Authorized = get()
    val router: AccountsRouter = get()
    authorized {
        get("/getAccount", callWrapper(router::getAccount))
    }
}


