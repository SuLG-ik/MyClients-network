package beauty.shafran.network.employees.repository

import beauty.shafran.network.employees.data.*


interface EmployeesRepository {

    suspend fun addEmployee(data: CreateEmployeeRequest): CreateEmployeeResponse
    suspend fun getAllEmployees(data: GetAllEmployeesRequest): GetAllEmployeesResponse
    suspend fun layoffEmployee(data: LayoffEmployeeRequest): LayoffEmployeeResponse
    suspend fun getEmployeeById(data: GetEmployeeByIdRequest): GetEmployeeByIdResponse
    suspend fun addEmployeeImage(data: AddEmployeeImageRequest): AddEmployeeImageResponse

}