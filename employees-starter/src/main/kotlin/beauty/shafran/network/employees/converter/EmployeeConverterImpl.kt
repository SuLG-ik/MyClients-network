package beauty.shafran.network.employees.converter

import beauty.shafran.network.companies.converter.CompanyConverter
import beauty.shafran.network.companies.data.Company
import beauty.shafran.network.companies.data.CompanyPlacement
import beauty.shafran.network.employees.data.Employee
import beauty.shafran.network.employees.data.EmployeeCompanyMember
import beauty.shafran.network.employees.data.EmployeeData
import beauty.shafran.network.employees.data.EmployeeId
import beauty.shafran.network.employees.tables.EmployeeCompanyEntity
import beauty.shafran.network.employees.tables.EmployeeDataEntity

class EmployeeConverterImpl(
    private val companyConverter: CompanyConverter,
) : EmployeeConverter {
    override fun toEmployee(employeeEntity: EmployeeCompanyEntity, employeeData: EmployeeDataEntity): Employee {
        return Employee(
            id = EmployeeId(employeeEntity.id),
            data = EmployeeData(name = employeeData.name),
        )
    }

    override fun toEmployeeCompanyMember(
        employeeEntity: EmployeeCompanyEntity,
        company: Company,
        placements: List<CompanyPlacement>,
    ): EmployeeCompanyMember {
        return EmployeeCompanyMember(
            employeeId = EmployeeId(employeeEntity.id),
            company = company,
            placements = placements
        )
    }


}