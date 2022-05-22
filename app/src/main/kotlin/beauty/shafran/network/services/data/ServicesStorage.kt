package beauty.shafran.network.services.data

import beauty.shafran.network.companies.data.CompanyId
import beauty.shafran.network.companies.data.CompanyStationId
import kotlinx.serialization.Serializable

@Serializable
class ServicesStorageEntity(
    val id: ServicesStorageId,
    val companies: List<CompanyId>,
    val stations: List<CompanyStationId>,
)

@Serializable
@JvmInline
value class ServicesStorageId(
    val id: Long,
)