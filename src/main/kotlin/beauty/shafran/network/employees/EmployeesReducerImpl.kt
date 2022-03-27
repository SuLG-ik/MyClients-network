package beauty.shafran.network.employees

import beauty.shafran.network.employees.data.CreateEmployeeRequest
import beauty.shafran.network.employees.data.GetAllEmployeesRequest
import beauty.shafran.network.employees.data.GetEmployeeByIdRequest
import beauty.shafran.network.employees.data.LayoffEmployeeRequest
import beauty.shafran.network.employees.repository.EmployeesRepository
import io.ktor.application.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.util.pipeline.*

class EmployeesReducerImpl(private val employeesRepository: EmployeesRepository) : EmployeesReducer {
    override val getAllEmployees: suspend PipelineContext<Unit, ApplicationCall>.(Unit) -> Unit = {
        val request = call.receive<GetAllEmployeesRequest>()
        val data = employeesRepository.getAllEmployees(request)
        call.respond(data)
    }
    override val createEmployee: suspend PipelineContext<Unit, ApplicationCall>.(Unit) -> Unit  = {
        val request = call.receive<CreateEmployeeRequest>()
        val data = employeesRepository.addEmployee(request)
        call.respond(data)
    }
    override val layoffEmployee: suspend PipelineContext<Unit, ApplicationCall>.(Unit) -> Unit = {
        val request = call.receive<LayoffEmployeeRequest>()
        val data = employeesRepository.layoffEmployee(request)
        call.respond(data)
    }
    override val getEmployeeById: suspend PipelineContext<Unit, ApplicationCall>.(Unit) -> Unit = {
        val request = call.receive<GetEmployeeByIdRequest>()
        val data = employeesRepository.getEmployeeById(request)
        call.respond(data)
    }
}