package beauty.shafran.network.employees

import beauty.shafran.network.api.withApi
import inject
import io.ktor.server.routing.*

fun Route.configureEmployees() {
    val reducer by inject<EmployeesReducer>()
    route("/employees") {
        withApi {
            get("/getAllEmployees", reducer.getAllEmployees)
            post("/createEmployee", reducer.createEmployee)
            delete("/layoffEmployee", reducer.layoffEmployee)
            get("/getEmployeeById", reducer.getEmployeeById)
        }
    }
}