package beauty.shafran.network.services.data

import beauty.shafran.network.companies.data.Company
import beauty.shafran.network.companies.data.CompanyId
import beauty.shafran.network.companies.data.CompanyPlacement
import beauty.shafran.network.companies.data.CompanyPlacementId
import kotlinx.serialization.Serializable


@JvmInline
@Serializable
value class ServiceId(
    val value: Long,
)

@JvmInline
@Serializable
value class ServiceCodename(
    val value: String,
)

@Serializable
data class ServiceData(
    val title: String,
    val description: String,
)

@Serializable
data class Service(
    val id: ServiceId,
    val codename: ServiceCodename,
    val data: ServiceData,
)

@Serializable
data class ServiceMember(
    val serviceId: ServiceId,
    val company: Company,
    val placements: List<CompanyPlacement>,
)