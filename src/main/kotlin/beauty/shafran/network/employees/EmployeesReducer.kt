package beauty.shafran.network.employees

import io.ktor.application.*
import io.ktor.util.pipeline.*

interface EmployeesReducer {

    val getAllEmployees: suspend PipelineContext<Unit, ApplicationCall>.(Unit) -> Unit
    val createEmployee: suspend PipelineContext<Unit, ApplicationCall>.(Unit) -> Unit
    val layoffEmployee: suspend PipelineContext<Unit, ApplicationCall>.(Unit) -> Unit
    val getEmployeeById: suspend PipelineContext<Unit, ApplicationCall>.(Unit) -> Unit

}