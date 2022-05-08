package beauty.shafran.network.employees

import beauty.shafran.network.employees.executor.EmployeesExecutor
import beauty.shafran.network.utils.callWrapper
import io.ktor.server.routing.*


fun Route.employeesRoute(
    executor: EmployeesExecutor,
) {
    get("/getAllEmployees", callWrapper(executor::getAllEmployees))
    post("/createEmployee", callWrapper(executor::createEmployee))
    delete("/layoffEmployee", callWrapper(executor::layoffEmployee))
    get("/getEmployeeById", callWrapper(executor::getEmployeeById))
}