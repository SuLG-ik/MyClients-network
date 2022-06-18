package beauty.shafran.network.services.route

import beauty.shafran.network.auth.AuthorizedAccount
import beauty.shafran.network.services.data.*


interface ServicesRouter {

    suspend fun getCompanyServices(
        request: GetAllCompanyServicesRequest,
        account: AuthorizedAccount,
    ): GetAllCompanyServicesResponse

    suspend fun getServiceById(request: GetServiceByIdRequest, account: AuthorizedAccount): GetServiceByIdResponse

    suspend fun getServicesByIds(request: GetServicesByIdsRequest, account: AuthorizedAccount): GetServicesByIdsResponse

    suspend fun createService(request: CreateServiceRequest, account: AuthorizedAccount): CreateServiceResponse

    suspend fun addServiceToPlacements(
        request: AddServiceToPlacementsRequest,
        account: AuthorizedAccount,
    ): AddServiceToPlacementsResponse

}