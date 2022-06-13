package beauty.shafran.network.companies.route

import beauty.shafran.network.auth.Authorized
import beauty.shafran.network.auth.callWrapper
import beauty.shafran.network.auth.invoke
import beauty.shafran.network.companies.schema.createAnnotatedMissingTablesAndColumns
import beauty.shafran.network.database.SetupTransactional
import beauty.shafran.network.get
import io.ktor.server.routing.*
import org.jetbrains.exposed.sql.SchemaUtils

fun Route.companiesRoute() {
    val authorized: Authorized = get()
    val router: CompaniesRouter = get()
    authorized {
        get("/getAvailableCompanies", callWrapper(router::getAvailableCompanies))
        post("/createCompany", callWrapper(router::createCompany))
        post("/createCompanyPlacement", callWrapper(router::createCompanyPlacement))
    }
}


