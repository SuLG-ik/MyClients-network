package beauty.shafran.network.employees.converter

import beauty.shafran.network.companies.data.CompanyId
import beauty.shafran.network.companies.data.CompanyPlacementId
import beauty.shafran.network.employees.data.Employee
import beauty.shafran.network.employees.data.EmployeeCompanyMember
import beauty.shafran.network.employees.data.EmployeeData
import beauty.shafran.network.employees.data.EmployeeId
import beauty.shafran.network.employees.tables.EmployeeCompanyEntity
import beauty.shafran.network.employees.tables.EmployeeDataEntity
import beauty.shafran.network.employees.tables.EmployeePlacementCompanyEntity

class EmployeeConverterImpl : EmployeeConverter {

    override fun toEmployee(
        employeeEntity: EmployeeCompanyEntity,
        employeeData: EmployeeDataEntity,
        placements: List<EmployeePlacementCompanyEntity>,
    ): Employee {
        return Employee(
            id = EmployeeId(employeeEntity.id),
            data = EmployeeData(name = employeeData.name),
            member = toEmployeeCompanyMember(employeeEntity, placements)
        )
    }

    override fun toEmployeeCompanyMember(
        employeeEntity: EmployeeCompanyEntity,
        placements: List<EmployeePlacementCompanyEntity>,
    ): EmployeeCompanyMember {
        return EmployeeCompanyMember(
            employeeId = EmployeeId(employeeEntity.id),
            companyId = CompanyId(employeeEntity.companyId),
            placementsIds = placements.map { CompanyPlacementId(it.placementId) }
        )
    }

}