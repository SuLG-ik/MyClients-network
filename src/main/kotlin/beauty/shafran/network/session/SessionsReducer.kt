package beauty.shafran.network.session

import io.ktor.server.application.*
import io.ktor.util.pipeline.*

interface SessionsReducer {

    val getServiceSessionsForCustomer: suspend PipelineContext<Unit, ApplicationCall>.(Unit) -> Unit
    val createServiceSessionsForCustomer: suspend PipelineContext<Unit, ApplicationCall>.(Unit) -> Unit
    val useServiceSession: suspend PipelineContext<Unit, ApplicationCall>.(Unit) -> Unit

}