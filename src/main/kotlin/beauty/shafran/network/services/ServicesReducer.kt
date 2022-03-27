package beauty.shafran.network.services

import io.ktor.application.*
import io.ktor.util.pipeline.*

interface ServicesReducer {

    val createService: suspend PipelineContext<Unit, ApplicationCall>.(Unit) -> Unit
    val getAllServices: suspend PipelineContext<Unit, ApplicationCall>.(Unit) -> Unit
    val getServiceById: suspend PipelineContext<Unit, ApplicationCall>.(Unit) -> Unit
    val addConfiguration: suspend PipelineContext<Unit, ApplicationCall>.(Unit) -> Unit
    val deactivateConfiguration: suspend PipelineContext<Unit, ApplicationCall>.(Unit) -> Unit

}