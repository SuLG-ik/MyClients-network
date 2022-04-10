package beauty.shafran.network.services.executor

import beauty.shafran.network.services.data.*


interface ServicesExecutor {

    suspend fun getServices(request: GetAllServicesRequest): GetAllServicesResponse

    suspend fun createService(request: CreateServiceRequest): CreateServiceResponse

    suspend fun editService(request: EditServiceRequest): EditServiceResponse

    suspend fun getServiceById(request: GetServiceByIdRequest): GetServiceByIdResponse

    suspend fun addConfiguration(request: CreateConfigurationRequest): CreateConfigurationResponse

    suspend fun deactivateConfiguration(request: DeactivateServiceConfigurationRequest): DeactivateServiceConfigurationResponse

}