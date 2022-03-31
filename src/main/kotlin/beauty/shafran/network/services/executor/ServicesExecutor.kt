package beauty.shafran.network.services.executor

import beauty.shafran.network.services.data.*


interface ServicesExecutor {

    suspend fun getServices(data: GetAllServicesRequest): GetAllServicesResponse

    suspend fun createService(data: CreateServiceRequest): CreateServiceResponse

    suspend fun getServiceById(data: GetServiceByIdRequest): GetServiceByIdResponse

    suspend fun addConfiguration(data: CreateConfigurationRequest): CreateConfigurationResponse

    suspend fun deactivateConfiguration(data: DeactivateServiceConfigurationRequest): DeactivateServiceConfigurationResponse

}