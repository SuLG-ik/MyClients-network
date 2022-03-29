package beauty.shafran.network.customers

import beauty.shafran.network.customers.data.*
import beauty.shafran.network.customers.repository.CustomersRepository
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.util.pipeline.*

class CustomersReducerImpl(
    private val customersRepository: CustomersRepository,
) : CustomersReducer {
    override val searchCustomerByPhone: suspend PipelineContext<Unit, ApplicationCall>.(Unit) -> Unit = {
        val request = call.receive<SearchCustomerByPhoneRequest>()
        call.respond(customersRepository.searchCustomerByPhone(request))
    }

    override val createCustomer: suspend PipelineContext<Unit, ApplicationCall>.(Unit) -> Unit = {
        val request = call.receive<CreateCustomersRequest>()
        call.respond(customersRepository.createCustomer(request))
    }

    override val restoreCustomer: suspend PipelineContext<Unit, ApplicationCall>.(Unit) -> Unit = {
        val request = call.receive<RestoreCustomerRequest>()
        customersRepository.restoreCustomer(request)
        call.respond(HttpStatusCode.OK)
    }

    override val getCustomerById: suspend PipelineContext<Unit, ApplicationCall>.(Unit) -> Unit = {
        val request = call.receive<GetCustomerByIdRequest>()
        call.respond(customersRepository.getCustomerById(request))
    }

    override val getCustomerByToken: suspend PipelineContext<Unit, ApplicationCall>.(Unit) -> Unit = {
        val request = call.receive<GetCustomerByTokenRequest>()
        call.respond(customersRepository.getCustomerByToken(request))
    }

    override val getAllCustomers: suspend PipelineContext<Unit, ApplicationCall>.(Unit) -> Unit = {
        val request = call.receive<GetAllCustomersRequest>()
        call.respond(customersRepository.getAllCustomers(request))
    }

    override val createEmptyCustomers: suspend PipelineContext<Unit, ApplicationCall>.(Unit) -> Unit = {
        val request = call.receive<CreateEmptyCustomersRequest>()
        call.respond(customersRepository.createEmptyCustomers(request))
    }

    override val editCustomerData: suspend PipelineContext<Unit, ApplicationCall>.(Unit) -> Unit = {
        val request = call.receive<EditCustomerRequest>()
        call.respond(customersRepository.editCustomerData(request))
    }

}