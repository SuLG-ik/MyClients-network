package beauty.shafran.network.employees.repository

import beauty.shafran.network.companies.data.CompanyId
import beauty.shafran.network.companies.data.CompanyPlacementId
import beauty.shafran.network.database.TransactionalScope
import beauty.shafran.network.employees.data.EmployeeId
import beauty.shafran.network.employees.tables.EmployeeCompanyEntity
import beauty.shafran.network.employees.tables.EmployeeDataEntity
import beauty.shafran.network.employees.tables.EmployeePlacementCompanyEntity


interface EmployeeRepository {

    context(TransactionalScope)  fun addEmployeeToCompanyPlacement(
        employeeId: EmployeeId,
        placementIds: List<CompanyPlacementId>,
    ): List<EmployeePlacementCompanyEntity>

    context(TransactionalScope)  fun createEmployee(
        name: String,
        description: String,
        companyId: CompanyId,
    ): Pair<EmployeeCompanyEntity, EmployeeDataEntity>

    context(TransactionalScope)  fun getEmployeePlacements(employeeId: EmployeeId): List<EmployeePlacementCompanyEntity>

    context(TransactionalScope) suspend fun getEmployeeEntityAndData(employeeId: EmployeeId): Pair<EmployeeCompanyEntity, EmployeeDataEntity>?

    context(TransactionalScope) suspend fun getEmployeeEntity(employeeId: EmployeeId): EmployeeCompanyEntity?

    context(TransactionalScope) suspend fun getCompanyEmployeeEntities(companyId: CompanyId): List<EmployeeCompanyEntity>
    context(TransactionalScope) suspend fun getCompanyEmployeeEntitiesAndData(companyId: CompanyId): List<Pair<EmployeeCompanyEntity, EmployeeDataEntity>>

    context(TransactionalScope) suspend fun getPlacementEmployeeEntities(placementIds: List<CompanyPlacementId>): List<EmployeeCompanyEntity>
    context(TransactionalScope) suspend fun getPlacementEmployeeEntitiesAndData(placementIds: List<CompanyPlacementId>): List<Pair<EmployeeCompanyEntity, EmployeeDataEntity>>
}