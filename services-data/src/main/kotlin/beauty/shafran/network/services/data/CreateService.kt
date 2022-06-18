package beauty.shafran.network.services.data

import beauty.shafran.network.companies.data.CompanyId
import beauty.shafran.network.companies.data.CompanyPlacementId
import kotlinx.serialization.Serializable

@Serializable
class CreateServiceRequest(
    val data: CreateServiceRequestData,
    val codename: ServiceCodename,
    val companyId: CompanyId,
)

@Serializable
class CreateServiceRequestData(
    val title: String,
    val description: String,
)

@Serializable
class CreateServiceResponse(
    val service: Service,
)

@Serializable
class AddServiceToPlacementsRequest(
    val serviceId: ServiceId,
    val placementsIds: List<CompanyPlacementId>,
)

@Serializable
class AddServiceToPlacementsResponse(
    val member: ServiceMember,
)