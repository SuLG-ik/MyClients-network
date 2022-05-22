package beauty.shafran.network.employees.converters

import beauty.shafran.network.assets.entity.AssetEntity
import beauty.shafran.network.employees.data.Employee
import beauty.shafran.network.employees.entity.EmployeeDataEntity
import beauty.shafran.network.employees.entity.EmployeeEntity
import beauty.shafran.network.employees.entity.EmployeeLayoffEntity

interface EmployeesConverter {

    fun buildEmployee(
        employeeEntity: EmployeeEntity,
        employeeDataEntity: EmployeeDataEntity,
        employeeLayoffEntity: EmployeeLayoffEntity?,
        image: AssetEntity?,
    ): Employee

}