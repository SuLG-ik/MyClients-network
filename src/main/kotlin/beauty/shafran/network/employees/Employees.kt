package beauty.shafran.network.employees

import io.ktor.routing.*
import org.koin.ktor.ext.inject

fun Routing.configureEmployees() {
    val reducer by inject<EmployeesReducer>()
    route("/employees") {
        get("/getAllEmployees", reducer.getAllEmployees)
        post("/createEmployee", reducer.createEmployee)
        delete("/layoffEmployee", reducer.layoffEmployee)
        get("/getEmployeeById", reducer.getEmployeeById)
    }
}