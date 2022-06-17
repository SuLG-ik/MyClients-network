package beauty.shafran.network.employees.repository

import beauty.shafran.network.companies.data.CompanyId
import beauty.shafran.network.companies.data.CompanyPlacementId
import beauty.shafran.network.database.TransactionalScope
import beauty.shafran.network.employees.data.EmployeeAlreadyInCompanyPlacement
import beauty.shafran.network.employees.data.EmployeeId
import beauty.shafran.network.employees.tables.*
import org.jetbrains.exposed.sql.JoinType
import org.jetbrains.exposed.sql.batchInsert
import org.jetbrains.exposed.sql.select

internal class EmployeeRepositoryImpl : EmployeeRepository {

    context (TransactionalScope) override fun createEmployee(
        name: String,
        description: String,
        companyId: CompanyId,
    ): Pair<EmployeeCompanyEntity, EmployeeDataEntity> {
        val employee = EmployeeCompanyTable.insertAndGetEntity(companyId = companyId.value)
        val employeeData =
            EmployeeDataTable.insertAndGetEntity(employeeId = employee.id, name = name, description = description)
        return employee to employeeData
    }

    context (TransactionalScope) override fun addEmployeeToCompanyPlacement(
        employeeId: EmployeeId,
        placementIds: List<CompanyPlacementId>,
    ): List<EmployeePlacementCompanyEntity> {
        if (getEmployeePlacements(employeeId).map { CompanyPlacementId(it.placementId) }.any { it in placementIds })
            throw EmployeeAlreadyInCompanyPlacement()
        val placements = EmployeePlacementCompanyTable.batchInsert(placementIds) { placement ->
            this[EmployeePlacementCompanyTable.employeeId] = employeeId.value
            this[EmployeePlacementCompanyTable.placementId] = placement.value
        }.map { it.toEmployeePlacementCompanyEntity() }
        return placements
    }

    context (TransactionalScope) override suspend fun getEmployeeEntityAndData(employeeId: EmployeeId): Pair<EmployeeCompanyEntity, EmployeeDataEntity>? {
        return EmployeeCompanyTable.join(
            EmployeeDataTable,
            JoinType.RIGHT,
            additionalConstraint = { EmployeeCompanyTable.id eq EmployeeDataTable.employeeId })
            .select { EmployeeCompanyTable.id eq employeeId.value }
            .firstOrNull()
            ?.let { it.toEmployeeCompanyEntity() to it.toEmployeeDataEntity() }
    }

    context (TransactionalScope) override suspend fun getEmployeeEntity(employeeId: EmployeeId): EmployeeCompanyEntity? {
        return EmployeeCompanyTable.findById(employeeId.value)
    }

    context (TransactionalScope) override fun getEmployeePlacements(employeeId: EmployeeId): List<EmployeePlacementCompanyEntity> {
        return EmployeePlacementCompanyTable.select { EmployeePlacementCompanyTable.employeeId eq employeeId.value }
            .map { it.toEmployeePlacementCompanyEntity() }
    }

    context(TransactionalScope) override suspend fun getCompanyEmployeeEntities(
        companyId: CompanyId,
    ): List<EmployeeCompanyEntity> {
        return EmployeeCompanyTable.select { EmployeeCompanyTable.companyId eq companyId.value }
            .map { it.toEmployeeCompanyEntity() }
    }

    context(TransactionalScope) override suspend fun getPlacementEmployeeEntities(
        placementIds: List<CompanyPlacementId>,
    ): List<EmployeeCompanyEntity> {
        return EmployeePlacementCompanyTable.join(
            EmployeeCompanyTable,
            JoinType.RIGHT,
            additionalConstraint = { EmployeePlacementCompanyTable.employeeId eq EmployeeCompanyTable.id },
        )
            .select { EmployeePlacementCompanyTable.placementId inList placementIds.map { it.value } }
            .map { it.toEmployeeCompanyEntity() }
    }

    context(TransactionalScope) override suspend fun getCompanyEmployeeEntitiesAndData(
        companyId: CompanyId,
    ): List<Pair<EmployeeCompanyEntity, EmployeeDataEntity>> {
        return EmployeeCompanyTable.join(
            EmployeeDataTable,
            JoinType.RIGHT,
            additionalConstraint = { EmployeeCompanyTable.id eq EmployeeDataTable.employeeId })
            .select { EmployeeCompanyTable.companyId eq companyId.value }
            .map { it.toEmployeeCompanyEntity() to it.toEmployeeDataEntity() }
    }

    context(TransactionalScope) override suspend fun getPlacementEmployeeEntitiesAndData(
        placementIds: List<CompanyPlacementId>,
    ): List<Pair<EmployeeCompanyEntity, EmployeeDataEntity>> {
        return EmployeePlacementCompanyTable.join(
            EmployeeCompanyTable,
            JoinType.RIGHT,
            additionalConstraint = { EmployeePlacementCompanyTable.employeeId eq EmployeeCompanyTable.id },
        ).join(
            EmployeeDataTable,
            JoinType.RIGHT,
            additionalConstraint = { EmployeeCompanyTable.id eq EmployeeDataTable.employeeId }
        )
            .select { EmployeePlacementCompanyTable.placementId inList placementIds.map { it.value } }
            .map { it.toEmployeeCompanyEntity() to it.toEmployeeDataEntity() }
    }

}