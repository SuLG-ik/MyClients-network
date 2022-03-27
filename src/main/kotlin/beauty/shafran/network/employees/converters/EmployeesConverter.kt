package beauty.shafran.network.employees.converters

import beauty.shafran.network.employees.data.Employee
import beauty.shafran.network.employees.data.EmployeeData
import beauty.shafran.network.employees.data.EmployeeLayoff
import beauty.shafran.network.employees.data.LayoffEmployeeRequest
import beauty.shafran.network.employees.entity.EmployeeDataEntity
import beauty.shafran.network.employees.entity.EmployeeEntity
import beauty.shafran.network.employees.entity.EmployeeLayoffEntity

interface EmployeesConverter {

    suspend fun EmployeeEntity.toData(): Employee

    suspend fun LayoffEmployeeRequest.toNewEntity(): EmployeeLayoffEntity

    suspend fun EmployeeDataEntity.toData(): EmployeeData
    suspend fun EmployeeLayoffEntity.toData(): EmployeeLayoff
}