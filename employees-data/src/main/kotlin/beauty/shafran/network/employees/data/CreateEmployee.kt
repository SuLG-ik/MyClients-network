package beauty.shafran.network.employees.data

import beauty.shafran.network.companies.data.CompanyId
import beauty.shafran.network.companies.data.CompanyPlacementId
import kotlinx.serialization.Serializable

@Serializable
data class CreateEmployeeRequest(
    val data: EmployeeCreateRequestData,
    val companyId: CompanyId,
    val companyPlacementIds: List<CompanyPlacementId> = emptyList(),
)


@Serializable
data class EmployeeCreateRequestData(
    val name: String,
    val description: String,
)

@Serializable
data class CreateEmployeeResponse(
    val employee: Employee,
)

@Serializable
data class AddEmployeeToCompanyPlacementsRequest(
    val employeeId: EmployeeId,
    val placementIds: List<CompanyPlacementId>,
)

@Serializable
data class AddEmployeeToCompanyPlacementsResponse(
    val employeeCompanyMember: EmployeeCompanyMember,
)