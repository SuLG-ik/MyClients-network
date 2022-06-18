package beauty.shafran.network.services.route

import beauty.shafran.network.auth.AuthorizedAccount
import beauty.shafran.network.database.Transactional
import beauty.shafran.network.services.converter.ServiceConverter
import beauty.shafran.network.services.data.*
import beauty.shafran.network.services.repository.ServiceRepository

internal class ServicesRouterImpl(
    private val transactional: Transactional,
    private val converter: ServiceConverter,
    private val serviceRepository: ServiceRepository,
) : ServicesRouter {
    override suspend fun getCompanyServices(
        request: GetAllCompanyServicesRequest,
        account: AuthorizedAccount,
    ): GetAllCompanyServicesResponse {
        TODO("Not yet implemented")
    }

    override suspend fun getServiceById(
        request: GetServiceByIdRequest,
        account: AuthorizedAccount,
    ): GetServiceByIdResponse {
        TODO("Not yet implemented")
    }

    override suspend fun getServicesByIds(
        request: GetServicesByIdsRequest,
        account: AuthorizedAccount,
    ): GetServicesByIdsResponse {
        TODO("Not yet implemented")
    }

    override suspend fun createService(
        request: CreateServiceRequest,
        account: AuthorizedAccount,
    ): CreateServiceResponse {
        TODO("Not yet implemented")
    }

    override suspend fun addServiceToPlacements(
        request: AddServiceToPlacementsRequest,
        account: AuthorizedAccount,
    ): AddServiceToPlacementsResponse {
        TODO("Not yet implemented")
    }

}