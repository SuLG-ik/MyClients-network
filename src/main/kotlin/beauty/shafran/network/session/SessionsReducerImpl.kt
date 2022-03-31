package beauty.shafran.network.session

import beauty.shafran.network.receiveOrThrow
import beauty.shafran.network.session.data.CreateSessionForCustomerRequest
import beauty.shafran.network.session.data.GetSessionUsagesHistoryRequest
import beauty.shafran.network.session.data.GetSessionsForCustomerRequest
import beauty.shafran.network.session.data.UseSessionRequest
import beauty.shafran.network.session.executor.SessionsExecutor
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.util.pipeline.*

class SessionsReducerImpl(
    private val sessionsRepository: SessionsExecutor,
    ) : SessionsReducer {

    override val getSessionUsagesHistory: suspend PipelineContext<Unit, ApplicationCall>.(Unit) -> Unit= {
        val request = call.receiveOrThrow<GetSessionUsagesHistoryRequest>()
        val data = sessionsRepository.getSessionUsagesHistory(request)
        call.respond(data)
    }
    override val getServiceSessionsForCustomer: suspend PipelineContext<Unit, ApplicationCall>.(Unit) -> Unit = {
        val request = call.receiveOrThrow<GetSessionsForCustomerRequest>()
        val data = sessionsRepository.getSessionsForCustomer(request)
        call.respond(data)
    }
    override val createServiceSessionsForCustomer: suspend PipelineContext<Unit, ApplicationCall>.(Unit) -> Unit = {
        val request = call.receiveOrThrow<CreateSessionForCustomerRequest>()
        val data = sessionsRepository.createSessionsForCustomer(request)
        call.respond(data)
    }
    override val useServiceSession: suspend PipelineContext<Unit, ApplicationCall>.(Unit) -> Unit = {
        val request = call.receiveOrThrow<UseSessionRequest>()
        val data = sessionsRepository.useSession(request)
        call.respond(data)
    }
}