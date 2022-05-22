package beauty.shafran.network.services.executor

import beauty.shafran.network.auth.data.AuthorizedAccount
import beauty.shafran.network.services.data.*


interface ServicesExecutor {

    suspend fun getServices(request: GetAllServicesRequest, account: AuthorizedAccount): GetAllServicesResponse

    suspend fun createService(request: CreateServiceRequest, account: AuthorizedAccount): CreateServiceResponse

    suspend fun editService(request: EditServiceRequest, account: AuthorizedAccount): EditServiceResponse

    suspend fun getServiceById(request: GetServiceByIdRequest, account: AuthorizedAccount): GetServiceByIdResponse

    suspend fun addConfiguration(request: CreateConfigurationRequest, account: AuthorizedAccount): CreateConfigurationResponse

    suspend fun deactivateConfiguration(request: DeactivateServiceConfigurationRequest, account: AuthorizedAccount): DeactivateServiceConfigurationResponse

}