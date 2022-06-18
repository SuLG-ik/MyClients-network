package beauty.shafran.network.employees.data

import beauty.shafran.network.companies.data.Company
import beauty.shafran.network.companies.data.CompanyPlacement
import kotlinx.serialization.Serializable

@Serializable
class EmployeeCompanyMember(
    val employeeId: EmployeeId,
    val company: Company,
    val placements: List<CompanyPlacement>,
)