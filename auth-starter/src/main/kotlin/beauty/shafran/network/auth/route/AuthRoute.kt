package beauty.shafran.network.auth.route

import beauty.shafran.network.auth.Authorized
import beauty.shafran.network.auth.callWrapper
import beauty.shafran.network.auth.invoke
import beauty.shafran.network.auth.schema.createAnnotatedMissingTablesAndColumns
import beauty.shafran.network.database.SetupTransactional
import beauty.shafran.network.get
import io.ktor.server.routing.*
import org.jetbrains.exposed.sql.SchemaUtils

fun Route.authRoute() {
    val authorized = get<Authorized>()
    val router = get<AuthRouter>()
    authorized {
        post("/refresh", callWrapper(router::refresh))
    }
    post("/login", callWrapper(router::login))
    post("/register", callWrapper(router::register))
}


