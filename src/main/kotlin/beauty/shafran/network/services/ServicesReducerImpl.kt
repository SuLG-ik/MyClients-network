package beauty.shafran.network.services

import beauty.shafran.network.services.data.*
import beauty.shafran.network.services.repository.MongoServicesRepository
import beauty.shafran.network.services.repository.ServicesRepository
import io.ktor.application.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.util.pipeline.*

class ServicesReducerImpl(
    private val servicesRepository: ServicesRepository,
) : ServicesReducer {
    override val createService: suspend PipelineContext<Unit, ApplicationCall>.(Unit) -> Unit = {
        val request = call.receive<CreateServiceRequest>()
        call.respond(servicesRepository.createService(request))
    }
    override val getAllServices: suspend PipelineContext<Unit, ApplicationCall>.(Unit) -> Unit = {
        val request = call.receive<GetAllServicesRequest>()
        call.respond(servicesRepository.getServices(request))
    }
    override val getServiceById: suspend PipelineContext<Unit, ApplicationCall>.(Unit) -> Unit = {
        val request = call.receive<GetServiceByIdRequest>()
        call.respond(servicesRepository.getServiceById(request))
    }
    override val addConfiguration: suspend PipelineContext<Unit, ApplicationCall>.(Unit) -> Unit = {
        val request = call.receive<CreateConfigurationRequest>()
        call.respond(servicesRepository.addConfiguration(request))
    }
    override val deactivateConfiguration: suspend PipelineContext<Unit, ApplicationCall>.(Unit) -> Unit = {
        val request = call.receive<DeactivateServiceConfigurationRequest>()
        call.respond(servicesRepository.deactivateConfiguration(request))
    }
}