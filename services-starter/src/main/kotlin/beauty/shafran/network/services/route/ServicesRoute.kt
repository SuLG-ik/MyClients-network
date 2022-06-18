package beauty.shafran.network.services.route

import beauty.shafran.network.auth.Authorized
import beauty.shafran.network.auth.callWrapper
import beauty.shafran.network.auth.invoke
import beauty.shafran.network.database.SetupTransactional
import beauty.shafran.network.get
import beauty.shafran.network.services.schema.createAnnotatedMissingTablesAndColumns
import io.ktor.server.routing.*
import org.jetbrains.exposed.sql.SchemaUtils

fun Route.servicesRoute() {
    get<SetupTransactional>().setup {
        SchemaUtils.createAnnotatedMissingTablesAndColumns()
    }
    val authorized: Authorized = get()
    val router: ServicesRouter = get()
    authorized {
        post("/createService", callWrapper(router::createService))
        put("/addServiceToPlacement", callWrapper(router::addServiceToPlacements))
        get("/getServiceById", callWrapper(router::getServiceById))
        get("/getServicesByIds", callWrapper(router::getServicesByIds))
    }
}


