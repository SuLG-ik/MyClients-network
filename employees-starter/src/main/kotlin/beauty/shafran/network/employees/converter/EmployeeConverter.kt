package beauty.shafran.network.employees.converter

import beauty.shafran.network.companies.data.Company
import beauty.shafran.network.companies.data.CompanyPlacement
import beauty.shafran.network.employees.data.Employee
import beauty.shafran.network.employees.data.EmployeeCompanyMember
import beauty.shafran.network.employees.tables.EmployeeCompanyEntity
import beauty.shafran.network.employees.tables.EmployeeDataEntity
import beauty.shafran.network.employees.tables.EmployeePlacementCompanyEntity

interface EmployeeConverter {

    fun toEmployee(
        employeeEntity: EmployeeCompanyEntity,
        employeeData: EmployeeDataEntity,
    ): Employee


    fun toEmployeeCompanyMember(
        employeeEntity: EmployeeCompanyEntity,
        company: Company,
        placements: List<CompanyPlacement>
    ): EmployeeCompanyMember

}