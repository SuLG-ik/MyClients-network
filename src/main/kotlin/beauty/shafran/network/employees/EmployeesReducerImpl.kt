package beauty.shafran.network.employees

import beauty.shafran.network.employees.data.CreateEmployeeRequest
import beauty.shafran.network.employees.data.GetAllEmployeesRequest
import beauty.shafran.network.employees.data.GetEmployeeByIdRequest
import beauty.shafran.network.employees.data.LayoffEmployeeRequest
import beauty.shafran.network.employees.executor.EmployeesExecutor
import beauty.shafran.network.employees.validators.EmployeesValidator
import beauty.shafran.network.receiveOrThrow
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.util.pipeline.*

class EmployeesReducerImpl(
    private val employeesExecutor: EmployeesExecutor,
    private val validator: EmployeesValidator,
) : EmployeesReducer {

    override val getAllEmployees: suspend PipelineContext<Unit, ApplicationCall>.(Unit) -> Unit = {
        val request = call.receiveOrThrow<GetAllEmployeesRequest>()
        val data = employeesExecutor.getAllEmployees(validator.getAllEmployees(request))
        call.respond(data)
    }
    override val createEmployee: suspend PipelineContext<Unit, ApplicationCall>.(Unit) -> Unit = {
        val request = call.receiveOrThrow<CreateEmployeeRequest>()
        val data = employeesExecutor.createEmployee(validator.createEmployee(request))
        call.respond(data)
    }

    override val layoffEmployee: suspend PipelineContext<Unit, ApplicationCall>.(Unit) -> Unit = {
        val request = call.receiveOrThrow<LayoffEmployeeRequest>()
        val data = employeesExecutor.layoffEmployee(validator.layoffEmployee(request))
        call.respond(data)
    }

    override val getEmployeeById: suspend PipelineContext<Unit, ApplicationCall>.(Unit) -> Unit = {
        val request = call.receiveOrThrow<GetEmployeeByIdRequest>()
        val data = employeesExecutor.getEmployeeById(validator.getEmployeeById(request))
        call.respond(data)
    }

}