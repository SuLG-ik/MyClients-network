package beauty.shafran.network.customers

import beauty.shafran.network.customers.data.*
import beauty.shafran.network.customers.executor.CustomersExecutor
import beauty.shafran.network.customers.validators.CustomersValidator
import beauty.shafran.network.receiveOrThrow
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.util.pipeline.*

class CustomersReducerImpl(
    private val executor: CustomersExecutor,
    private val validator: CustomersValidator,
) : CustomersReducer {
    override val searchCustomerByPhone: suspend PipelineContext<Unit, ApplicationCall>.(Unit) -> Unit = {
        val request = call.receiveOrThrow<SearchCustomerByPhoneRequest>()
        call.respond(executor.searchCustomerByPhone(validator.searchCustomerByPhone(request)))
    }

    override val createCustomer: suspend PipelineContext<Unit, ApplicationCall>.(Unit) -> Unit = {
        val request = call.receiveOrThrow<CreateCustomersRequest>()
        call.respond(executor.createCustomer(validator.createCustomer(request)))
    }

    override val restoreCustomer: suspend PipelineContext<Unit, ApplicationCall>.(Unit) -> Unit = {
        val request = call.receiveOrThrow<RestoreCustomerRequest>()
        executor.restoreCustomer(validator.restoreCustomer(request))
        call.respond(HttpStatusCode.OK)
    }

    override val getCustomerById: suspend PipelineContext<Unit, ApplicationCall>.(Unit) -> Unit = {
        val request = call.receiveOrThrow<GetCustomerByIdRequest>()
        call.respond(executor.getCustomerById(validator.getCustomerById(request)))
    }

    override val getCustomerByToken: suspend PipelineContext<Unit, ApplicationCall>.(Unit) -> Unit = {
        val request = call.receiveOrThrow<GetCustomerByTokenRequest>()
        call.respond(executor.getCustomerByToken(validator.getCustomerByToken(request)))
    }

    override val getAllCustomers: suspend PipelineContext<Unit, ApplicationCall>.(Unit) -> Unit = {
        val request = call.receiveOrThrow<GetAllCustomersRequest>()
        call.respond(executor.getAllCustomers(validator.getAllCustomers(request)))
    }

    override val createEmptyCustomers: suspend PipelineContext<Unit, ApplicationCall>.(Unit) -> Unit = {
        val request = call.receiveOrThrow<CreateEmptyCustomersRequest>()
        call.respond(executor.createEmptyCustomers(validator.createEmptyCustomers(request)))
    }

    override val editCustomerData: suspend PipelineContext<Unit, ApplicationCall>.(Unit) -> Unit = {
        val request = call.receiveOrThrow<EditCustomerRequest>()
        call.respond(executor.editCustomerData(validator.editCustomerData(request)))
    }

}