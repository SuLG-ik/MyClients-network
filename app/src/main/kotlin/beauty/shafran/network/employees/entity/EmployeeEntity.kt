package beauty.shafran.network.employees.entity

import beauty.shafran.network.gender.genderEnumeration
import beauty.shafran.network.utils.LongIdWithMetaTable
import ru.sulgik.exposed.PropertyOfEntity
import ru.sulgik.exposed.TableToCreation
import ru.sulgik.exposed.TableWithEntity

@TableToCreation
@TableWithEntity
object EmployeeTable : LongIdWithMetaTable("employee")

@TableToCreation
@TableWithEntity
object EmployeeDataTable : LongIdWithMetaTable("employee_data") {
    @PropertyOfEntity
    val name = varchar("name", 50)
    @PropertyOfEntity
    val gender = genderEnumeration("genders")
    @PropertyOfEntity
    val description = text("description")
    @PropertyOfEntity
    val employeeId = reference("employee", EmployeeTable)
}

@TableToCreation
@TableWithEntity
object EmployeeLayoffTable : LongIdWithMetaTable("employee_layoff") {
    @PropertyOfEntity
    val note = varchar("note", 100)

    @PropertyOfEntity
    val employeeId = reference("employee", EmployeeTable)
}

@TableToCreation
@TableWithEntity
object EmployeeToStorageTable : LongIdWithMetaTable("employee_to_storage") {
    @PropertyOfEntity
    val employeeId = reference("employee ", EmployeeTable)
    @PropertyOfEntity
    val storageId = reference("storage", EmployeesStoragesTable)
}

