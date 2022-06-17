package beauty.shafran.network.employees.converter

import beauty.shafran.network.employees.data.Employee
import beauty.shafran.network.employees.data.EmployeeCompanyMember
import beauty.shafran.network.employees.tables.EmployeeCompanyEntity
import beauty.shafran.network.employees.tables.EmployeeDataEntity
import beauty.shafran.network.employees.tables.EmployeePlacementCompanyEntity

interface EmployeeConverter {

    fun toEmployee(
        employeeEntity: EmployeeCompanyEntity,
        employeeData: EmployeeDataEntity,
        placements: List<EmployeePlacementCompanyEntity>,
    ): Employee

    fun toEmployeeCompanyMember(
        employeeEntity: EmployeeCompanyEntity,
        placements: List<EmployeePlacementCompanyEntity>
    ): EmployeeCompanyMember
}