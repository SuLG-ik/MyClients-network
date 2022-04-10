package beauty.shafran.network.services

import beauty.shafran.network.services.data.*
import beauty.shafran.network.services.executor.ServicesExecutor
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController
import javax.validation.Valid

@RequestMapping("/v1/services")
@RestController
class Services(
    private val executor: ServicesExecutor,
) {

    @RequestMapping(
        value = ["/getAllServices"],
        method = [RequestMethod.GET],
        produces = [MediaType.APPLICATION_JSON_VALUE],
        consumes = [MediaType.APPLICATION_JSON_VALUE],
    )
    suspend fun getAllServices(@Valid @RequestBody request: GetAllServicesRequest): GetAllServicesResponse {
        return executor.getServices(request)
    }

    @RequestMapping(
        value = ["/getServiceById"],
        method = [RequestMethod.GET],
        produces = [MediaType.APPLICATION_JSON_VALUE],
        consumes = [MediaType.APPLICATION_JSON_VALUE],
    )
    suspend fun getServiceById(@Valid @RequestBody request: GetServiceByIdRequest): GetServiceByIdResponse {
        return executor.getServiceById(request)
    }


    @RequestMapping(
        value = ["/createService"],
        method = [RequestMethod.POST],
        produces = [MediaType.APPLICATION_JSON_VALUE],
        consumes = [MediaType.APPLICATION_JSON_VALUE],
    )
    suspend fun createService(@Valid @RequestBody request: CreateServiceRequest): CreateServiceResponse {
        return executor.createService(request)
    }

    @RequestMapping(
        value = ["/editService"],
        method = [RequestMethod.PATCH],
        produces = [MediaType.APPLICATION_JSON_VALUE],
        consumes = [MediaType.APPLICATION_JSON_VALUE],
    )
    suspend fun editService(@Valid @RequestBody request: EditServiceRequest): EditServiceResponse {
        return executor.editService(request)
    }


    @RequestMapping(
        value = ["/createConfiguration"],
        method = [RequestMethod.PUT],
        produces = [MediaType.APPLICATION_JSON_VALUE],
        consumes = [MediaType.APPLICATION_JSON_VALUE],
    )
    suspend fun addConfiguration(@Valid @RequestBody request: CreateConfigurationRequest): CreateConfigurationResponse {
        return executor.addConfiguration(request)
    }

    @RequestMapping(
        value = ["/deactivateConfiguration"],
        method = [RequestMethod.DELETE],
        produces = [MediaType.APPLICATION_JSON_VALUE],
        consumes = [MediaType.APPLICATION_JSON_VALUE],
    )
    suspend fun deactivateConfiguration(@Valid @RequestBody request: DeactivateServiceConfigurationRequest): DeactivateServiceConfigurationResponse {
        return executor.deactivateConfiguration(request)
    }

}


//fun Route.configureServices() {
//    val reducer by inject<ServicesReducer>()
//
//    route("/services") {
//        withApi {
//            get("/getAllServices", reducer.getAllServices)
//            get("/getServiceById", reducer.getServiceById)
//            post("/createService", reducer.createService)
//            put("/addConfiguration", reducer.addConfiguration)
//            delete("/deactivateConfiguration", reducer.deactivateConfiguration)
//        }
//    }
//}