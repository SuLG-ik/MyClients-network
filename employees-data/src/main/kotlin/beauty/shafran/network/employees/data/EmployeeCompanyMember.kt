package beauty.shafran.network.employees.data

import beauty.shafran.network.companies.data.CompanyId
import beauty.shafran.network.companies.data.CompanyPlacementId
import kotlinx.serialization.Serializable

@Serializable
class EmployeeCompanyMember(
    val employeeId: EmployeeId,
    val companyId: CompanyId,
    val placementsIds: List<CompanyPlacementId>,
)