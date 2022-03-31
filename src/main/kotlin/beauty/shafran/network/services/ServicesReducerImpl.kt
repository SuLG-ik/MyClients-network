package beauty.shafran.network.services

import beauty.shafran.network.receiveOrThrow
import beauty.shafran.network.services.data.*
import beauty.shafran.network.services.executor.ServicesExecutor
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.util.pipeline.*

class ServicesReducerImpl(
    private val servicesExecutor: ServicesExecutor,
) : ServicesReducer {
    override val createService: suspend PipelineContext<Unit, ApplicationCall>.(Unit) -> Unit = {
        val request = call.receiveOrThrow<CreateServiceRequest>()
        call.respond(servicesExecutor.createService(request))
    }
    override val getAllServices: suspend PipelineContext<Unit, ApplicationCall>.(Unit) -> Unit = {
        val request = call.receiveOrThrow<GetAllServicesRequest>()
        call.respond(servicesExecutor.getServices(request))
    }
    override val getServiceById: suspend PipelineContext<Unit, ApplicationCall>.(Unit) -> Unit = {
        val request = call.receiveOrThrow<GetServiceByIdRequest>()
        call.respond(servicesExecutor.getServiceById(request))
    }
    override val addConfiguration: suspend PipelineContext<Unit, ApplicationCall>.(Unit) -> Unit = {
        val request = call.receiveOrThrow<CreateConfigurationRequest>()
        call.respond(servicesExecutor.addConfiguration(request))
    }
    override val deactivateConfiguration: suspend PipelineContext<Unit, ApplicationCall>.(Unit) -> Unit = {
        val request = call.receiveOrThrow<DeactivateServiceConfigurationRequest>()
        call.respond(servicesExecutor.deactivateConfiguration(request))
    }
}