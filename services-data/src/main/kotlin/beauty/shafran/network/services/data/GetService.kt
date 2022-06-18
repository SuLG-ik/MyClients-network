package beauty.shafran.network.services.data

import beauty.shafran.network.companies.data.CompanyId
import beauty.shafran.network.paged.data.PagedDataRequest
import beauty.shafran.network.paged.data.PagedDataResponse
import kotlinx.serialization.Serializable


@Serializable
data class GetServiceByIdRequest(
    val serviceId: ServiceId,
)

@Serializable
data class GetServiceByIdResponse(
    val service: Service,
)

@Serializable
data class GetServicesByIdsRequest(
    val servicesIds: List<ServiceId>,
)

@Serializable
data class GetServicesByIdsResponse(
    val services: List<Service>,
)

@Serializable
data class GetAllCompanyServicesRequest(
    val companyId: CompanyId,
    val pagedData: PagedDataRequest,
)

@Serializable
data class GetAllCompanyServicesResponse(
    val companyId: CompanyId,
    val pagedData: PagedDataResponse,
)
