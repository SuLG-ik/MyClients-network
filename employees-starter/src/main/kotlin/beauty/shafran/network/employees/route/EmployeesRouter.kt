package beauty.shafran.network.employees.route

import beauty.shafran.network.auth.AuthorizedAccount
import beauty.shafran.network.employees.data.*


interface EmployeesRouter {


    suspend fun createEmployee(request: CreateEmployeeRequest, account: AuthorizedAccount): CreateEmployeeResponse

    suspend fun addEmployeeToPlacements(
        request: AddEmployeeToCompanyPlacementsRequest,
        account: AuthorizedAccount,
    ): AddEmployeeToCompanyPlacementsResponse

    suspend fun getEmployeeById(request: GetEmployeeByIdRequest, account: AuthorizedAccount): GetEmployeeByIdResponse

    suspend fun getEmployeesByIds(
        request: GetEmployeesByIdsRequest,
        account: AuthorizedAccount,
    ): GetEmployeesByIdsResponse

    suspend fun getCompanyEmployees(
        request: GetCompanyEmployeesRequest,
        account: AuthorizedAccount,
    ): GetCompanyEmployeesResponse


    suspend fun getCompanyEmployeesAndLoad(
        request: GetCompanyEmployeesAndLoadRequest,
        account: AuthorizedAccount
    ): GetCompanyEmployeesAndLoadResponse

    suspend fun getPlacementEmployeesAndLoad(
        request: GetPlacementEmployeesAndLoadRequest,
        account: AuthorizedAccount
    ): GetPlacementEmployeesAndLoadResponse

    suspend fun getPlacementEmployees(
        request: GetPlacementEmployeesRequest,
        account: AuthorizedAccount
    ): GetPlacementEmployeesResponse
}