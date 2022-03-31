package beauty.shafran.network.employees.repository

import beauty.shafran.network.employees.entity.EmployeeDataEntity
import beauty.shafran.network.employees.entity.EmployeeEntity
import beauty.shafran.network.employees.entity.EmployeeLayoffEntity

interface EmployeesRepository {


    suspend fun throwIfEmployeeNotExists(employeeId: String)
    suspend fun isEmployeeExists(employeeId: String): Boolean
    suspend fun findEmployeeById(employeeId: String): EmployeeEntity
    suspend fun updateEmployeeData(employeeId: String, data: EmployeeDataEntity): EmployeeEntity
    suspend fun updateEmployeeLayoff(employeeId: String, data: EmployeeLayoffEntity): EmployeeEntity
    suspend fun insertEmployee(data: EmployeeDataEntity): EmployeeEntity
    suspend fun findAllEmployees(offset: Int, page: Int): List<EmployeeEntity>

}