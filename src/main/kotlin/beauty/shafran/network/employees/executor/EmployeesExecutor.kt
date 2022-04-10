package beauty.shafran.network.employees.executor

import beauty.shafran.network.employees.data.*


interface EmployeesExecutor {

    suspend fun createEmployee(request: CreateEmployeeRequest): CreateEmployeeResponse
    suspend fun getAllEmployees(request: GetAllEmployeesRequest): GetAllEmployeesResponse
    suspend fun layoffEmployee(request: LayoffEmployeeRequest): LayoffEmployeeResponse
    suspend fun getEmployeeById(request: GetEmployeeByIdRequest): GetEmployeeByIdResponse
    suspend fun addEmployeeImage(request: AddEmployeeImageRequest): AddEmployeeImageResponse

}