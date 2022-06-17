package beauty.shafran.network.employees.route

import beauty.shafran.network.auth.Authorized
import beauty.shafran.network.auth.callWrapper
import beauty.shafran.network.auth.invoke
import beauty.shafran.network.employees.schema.createAnnotatedMissingTablesAndColumns
import beauty.shafran.network.get
import io.ktor.server.routing.*
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction

fun Route.employeesRoute() {
    transaction {
        SchemaUtils.createAnnotatedMissingTablesAndColumns()
    }
    val authorized: Authorized = get()
    val router: EmployeesRouter = get()
    authorized {
        post("/createEmployee", callWrapper(router::createEmployee))
        put("/addEmployeeToPlacements", callWrapper(router::addEmployeeToPlacements))
        get("/getEmployeeById", callWrapper(router::getEmployeeById))
        get("/getEmployeesByIds", callWrapper(router::getEmployeesByIds))
        get("/getCompanyEmployees", callWrapper(router::getCompanyEmployees))
        get("/getCompanyEmployeesAndLoad", callWrapper(router::getCompanyEmployeesAndLoad))
        get("/getPlacementEmployees", callWrapper(router::getPlacementEmployees))
        get("/getPlacementEmployeesAndLoad", callWrapper(router::getPlacementEmployeesAndLoad))
    }
}


