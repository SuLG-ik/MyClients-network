package beauty.shafran.network.employees

import beauty.shafran.network.employees.data.*
import beauty.shafran.network.employees.executor.EmployeesExecutor
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController
import javax.validation.Valid

@RequestMapping("/v1/employees")
@RestController
class Employees(
    private val executor: EmployeesExecutor,
) {

    @RequestMapping(
        value = ["/getAllEmployees"],
        method = [RequestMethod.GET],
        produces = [MediaType.APPLICATION_JSON_VALUE],
        consumes = [MediaType.APPLICATION_JSON_VALUE],
    )
    suspend fun getAllEmployees(@Valid @RequestBody request: GetAllEmployeesRequest): GetAllEmployeesResponse {
        return executor.getAllEmployees(request)
    }


    @RequestMapping(
        value = ["/createEmployee"],
        method = [RequestMethod.POST],
        produces = [MediaType.APPLICATION_JSON_VALUE],
        consumes = [MediaType.APPLICATION_JSON_VALUE],
    )
    suspend fun createEmployee(@Valid @RequestBody request: CreateEmployeeRequest): CreateEmployeeResponse {
        return executor.createEmployee(request)
    }

    @RequestMapping(
        value = ["/layoffEmployee"],
        method = [RequestMethod.DELETE],
        produces = [MediaType.APPLICATION_JSON_VALUE],
        consumes = [MediaType.APPLICATION_JSON_VALUE],
    )
    suspend fun layoffEmployee(@Valid @RequestBody request: LayoffEmployeeRequest): LayoffEmployeeResponse {
        return executor.layoffEmployee(request)
    }


    @RequestMapping(
        value = ["/getEmployeeById"],
        method = [RequestMethod.GET],
        produces = [MediaType.APPLICATION_JSON_VALUE],
        consumes = [MediaType.APPLICATION_JSON_VALUE],
    )
    suspend fun getEmployeeById(@Valid @RequestBody request: GetEmployeeByIdRequest): GetEmployeeByIdResponse {
        return executor.getEmployeeById(request)
    }


}

//fun Route.configureEmployees() {
//    val reducer by inject<EmployeesReducer>()
//    route("/employees") {
//        withApi {
//            get("/getAllEmployees", reducer.getAllEmployees)
//            post("/createEmployee", reducer.createEmployee)
//            delete("/layoffEmployee", reducer.layoffEmployee)
//            get("/getEmployeeById", reducer.getEmployeeById)
//        }
//    }
//}