package beauty.shafran.network.employees.data

import beauty.shafran.network.companies.data.CompanyId
import beauty.shafran.network.companies.data.CompanyStationId
import kotlinx.serialization.Serializable

@Serializable
data class EmployeeStorage(
    val id: EmployeeStorageId,
    val companies: List<CompanyId>,
    val stations: List<CompanyStationId>,
)


@Serializable
@JvmInline
value class EmployeeStorageId(
    val id: Long,
)