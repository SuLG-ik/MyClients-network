package beauty.shafran.network.employees.data

import beauty.shafran.network.companies.data.CompanyId
import beauty.shafran.network.companies.data.CompanyPlacementId
import beauty.shafran.network.paged.data.PagedDataRequest
import beauty.shafran.network.paged.data.PagedDataResponse
import kotlinx.serialization.Serializable

@Serializable
class GetEmployeeByIdRequest(
    val employeeId: EmployeeId,
)

@Serializable
class GetEmployeeByIdResponse(
    val employee: Employee,
)

@Serializable
class GetEmployeesByIdsRequest(
    val employeeIds: List<EmployeeId>,
)

@Serializable
class GetEmployeesByIdsResponse(
    val employees: List<Employee>,
)

@Serializable
class GetCompanyEmployeesRequest(
    val companyId: CompanyId,
    val pagedData: PagedDataRequest,
)

@Serializable
class GetCompanyEmployeesResponse(
    val employeeIds: List<EmployeeId>,
    val pagedData: PagedDataResponse,
)

@Serializable
class GetCompanyEmployeesAndLoadRequest(
    val companyId: CompanyId,
)

@Serializable
class GetCompanyEmployeesAndLoadResponse(
    val employees: List<Employee>,
)


@Serializable
class GetPlacementEmployeesRequest(
    val placementsIds: List<CompanyPlacementId>,
)

@Serializable
class GetPlacementEmployeesResponse(
    val employeesIds: List<EmployeeId>,
)

@Serializable
class GetPlacementEmployeesAndLoadRequest(
    val placementsIds: List<CompanyPlacementId>,
)

@Serializable
class GetPlacementEmployeesAndLoadResponse(
    val employees: List<Employee>,
)
