package beauty.shafran.network.services.validators

import beauty.shafran.network.services.data.*


interface ServicesValidator {

    suspend fun getServices(request: GetAllServicesRequest): GetAllServicesRequest

    suspend fun createService(request: CreateServiceRequest): CreateServiceRequest

    suspend fun getServiceById(request: GetServiceByIdRequest): GetServiceByIdRequest

    suspend fun addConfiguration(request: CreateConfigurationRequest): CreateConfigurationRequest

    suspend fun deactivateConfiguration(request: DeactivateServiceConfigurationRequest): DeactivateServiceConfigurationRequest

}