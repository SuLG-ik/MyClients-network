package beauty.shafran.network.employees.executor

import beauty.shafran.network.auth.data.AuthorizedAccount
import beauty.shafran.network.employees.data.*


interface EmployeesExecutor {

    suspend fun createEmployee(request: CreateEmployeeRequest, account: AuthorizedAccount): CreateEmployeeResponse
    suspend fun getAllEmployees(request: GetAllEmployeesRequest, account: AuthorizedAccount): GetAllEmployeesResponse
    suspend fun layoffEmployee(request: LayoffEmployeeRequest, account: AuthorizedAccount): LayoffEmployeeResponse
    suspend fun getEmployeeById(request: GetEmployeeWithIdRequest, account: AuthorizedAccount): GetEmployeeByIdResponse
    suspend fun addEmployeeImage(request: AddEmployeeImageRequest, account: AuthorizedAccount): AddEmployeeImageResponse

}