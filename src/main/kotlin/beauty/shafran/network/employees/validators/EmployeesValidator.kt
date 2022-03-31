package beauty.shafran.network.employees.validators

import beauty.shafran.network.employees.data.CreateEmployeeRequest
import beauty.shafran.network.employees.data.GetAllEmployeesRequest
import beauty.shafran.network.employees.data.GetEmployeeByIdRequest
import beauty.shafran.network.employees.data.LayoffEmployeeRequest


interface EmployeesValidator {

    suspend fun createEmployee(data: CreateEmployeeRequest): CreateEmployeeRequest
    suspend fun getAllEmployees(data: GetAllEmployeesRequest): GetAllEmployeesRequest
    suspend fun layoffEmployee(data: LayoffEmployeeRequest): LayoffEmployeeRequest
    suspend fun getEmployeeById(data: GetEmployeeByIdRequest): GetEmployeeByIdRequest

}