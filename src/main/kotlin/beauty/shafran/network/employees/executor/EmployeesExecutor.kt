package beauty.shafran.network.employees.executor

import beauty.shafran.network.employees.data.*


interface EmployeesExecutor {

    suspend fun createEmployee(data: CreateEmployeeRequest): CreateEmployeeResponse
    suspend fun getAllEmployees(data: GetAllEmployeesRequest): GetAllEmployeesResponse
    suspend fun layoffEmployee(data: LayoffEmployeeRequest): LayoffEmployeeResponse
    suspend fun getEmployeeById(data: GetEmployeeByIdRequest): GetEmployeeByIdResponse
    suspend fun addEmployeeImage(data: AddEmployeeImageRequest): AddEmployeeImageResponse

}