package beauty.shafran.network.customers

import io.ktor.server.application.*
import io.ktor.util.pipeline.*

interface CustomersReducer {

    val searchCustomerByPhone: suspend PipelineContext<Unit, ApplicationCall>.(Unit) -> Unit
    val createCustomer: suspend PipelineContext<Unit, ApplicationCall>.(Unit) -> Unit
    val restoreCustomer: suspend PipelineContext<Unit, ApplicationCall>.(Unit) -> Unit
    val getCustomerById: suspend PipelineContext<Unit, ApplicationCall>.(Unit) -> Unit
    val getCustomerByToken: suspend PipelineContext<Unit, ApplicationCall>.(Unit) -> Unit
    val getAllCustomers: suspend PipelineContext<Unit, ApplicationCall>.(Unit) -> Unit
    val createEmptyCustomers: suspend PipelineContext<Unit, ApplicationCall>.(Unit) -> Unit
    val editCustomerData: suspend PipelineContext<Unit, ApplicationCall>.(Unit) -> Unit

}