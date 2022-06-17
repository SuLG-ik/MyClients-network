package beauty.shafran.network.employees.tables

import beauty.shafran.network.companies.tables.CompanyPlacementTable
import beauty.shafran.network.companies.tables.CompanyTable
import beauty.shafran.network.exposed.LongIdWithMetaTable
import ru.sulgik.exposed.PropertyOfEntity
import ru.sulgik.exposed.TableToCreation
import ru.sulgik.exposed.TableWithEntity

@TableToCreation
@TableWithEntity
object EmployeeCompanyTable : LongIdWithMetaTable("company_employee") {
    @PropertyOfEntity
    val companyId = reference("company_id", CompanyTable)
}

@TableToCreation
@TableWithEntity
object EmployeePlacementCompanyTable : LongIdWithMetaTable("company_placement_employee") {
    @PropertyOfEntity
    val employeeId = reference("employee_id", EmployeeCompanyTable)

    @PropertyOfEntity
    val placementId = reference("placement_id", CompanyPlacementTable)
}

@TableToCreation
@TableWithEntity
object EmployeeDataTable : LongIdWithMetaTable("company_employee_data") {
    @PropertyOfEntity
    val employeeId = reference("employee_id", EmployeeCompanyTable)

    @PropertyOfEntity
    val description = text("description")

    @PropertyOfEntity
    val name = varchar("name", 64)
}

