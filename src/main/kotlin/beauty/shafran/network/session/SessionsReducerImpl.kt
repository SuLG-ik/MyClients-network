package beauty.shafran.network.session

import beauty.shafran.network.session.data.CreateSessionForCustomerRequest
import beauty.shafran.network.session.data.GetSessionsForCustomerRequest
import beauty.shafran.network.session.data.UseSessionRequest
import beauty.shafran.network.session.repository.ServiceSessionsRepository
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.util.pipeline.*

class SessionsReducerImpl(
    private val sessionsRepository: ServiceSessionsRepository,
    ) : SessionsReducer {
    override val getServiceSessionsForCustomer: suspend PipelineContext<Unit, ApplicationCall>.(Unit) -> Unit = {
        val request = call.receive<GetSessionsForCustomerRequest>()
        val data = sessionsRepository.getSessionsForCustomer(request)
        call.respond(data)
    }
    override val createServiceSessionsForCustomer: suspend PipelineContext<Unit, ApplicationCall>.(Unit) -> Unit = {
        val request = call.receive<CreateSessionForCustomerRequest>()
        val data = sessionsRepository.createSessionsForCustomer(request)
        call.respond(data)
    }
    override val useServiceSession: suspend PipelineContext<Unit, ApplicationCall>.(Unit) -> Unit = {
        val request = call.receive<UseSessionRequest>()
        val data = sessionsRepository.useSession(request)
        call.respond(data)
    }
}