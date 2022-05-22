package beauty.shafran.network.employees.entity

import beauty.shafran.network.companies.entity.CompanyStationTable
import beauty.shafran.network.companies.entity.CompanyTable
import beauty.shafran.network.utils.LongIdWithMetaTable
import ru.sulgik.exposed.TableToCreation

@TableToCreation
object EmployeesStoragesTable: LongIdWithMetaTable("employees_storage")

@TableToCreation
object EmployeesStoragesToCompanyTable: LongIdWithMetaTable("employees_storage_to_company") {
    val storageId = reference("storage", EmployeesStoragesTable)
    val companyId = reference("company", CompanyTable)
}

@TableToCreation
object EmployeesStoragesToCompanyStationTable: LongIdWithMetaTable("employees_storage_to_company_station") {
    val storageId = reference("storage", EmployeesStoragesTable)
    val stationId = reference("station", CompanyStationTable)
}