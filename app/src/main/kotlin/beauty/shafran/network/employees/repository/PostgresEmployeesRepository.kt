package beauty.shafran.network.employees.repository

import beauty.shafran.EmployeeNotExistsWithId
import beauty.shafran.network.assets.entity.AssetEntity
import beauty.shafran.network.employees.data.CreateEmployeeRequestData
import beauty.shafran.network.employees.data.EmployeeId
import beauty.shafran.network.employees.data.EmployeeStorageId
import beauty.shafran.network.employees.data.LayoffEmployeeRequestData
import beauty.shafran.network.employees.entity.*
import beauty.shafran.network.utils.PagedData
import beauty.shafran.network.utils.TransactionalScope
import beauty.shafran.network.utils.isRowExists
import beauty.shafran.network.utils.selectLatest
import kotlinx.coroutines.awaitAll
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.jetbrains.exposed.sql.select
import org.koin.core.annotation.Single

@Single
class PostgresEmployeesRepository : EmployeesRepository {


    override suspend fun TransactionalScope.throwIfEmployeeNotExists(employeeId: EmployeeId) {
        if (!isEmployeeExists(employeeId)) throw EmployeeNotExistsWithId(employeeId.toString())
    }

    override suspend fun TransactionalScope.isEmployeeExists(employeeId: EmployeeId): Boolean {
        return EmployeeTable.isRowExists { EmployeeTable.id eq employeeId.id }
    }


    override suspend fun TransactionalScope.findEmployeeDataById(employeeId: EmployeeId): EmployeeDataEntity {
        return EmployeeDataTable.selectLatest { EmployeeDataTable.employeeId eq employeeId.id }?.toEmployeeDataEntity()
            ?: throw EmployeeNotExistsWithId(employeeId.toString())
    }

    override suspend fun TransactionalScope.findEmployeeLayoffById(employeeId: EmployeeId): EmployeeLayoffEntity {
        return EmployeeLayoffTable.selectLatest { EmployeeDataTable.employeeId eq employeeId.id }
            ?.toEmployeeLayoffEntity() ?: throw EmployeeNotExistsWithId(employeeId.toString())
    }

    override suspend fun TransactionalScope.findEmployeeImageById(employeeId: EmployeeId): AssetEntity {
        TODO("Not yet implemented")
    }

    override suspend fun TransactionalScope.findEmployeeById(
        employeeId: EmployeeId,
    ): EmployeeEntity {
        return EmployeeTable.findById(employeeId.id) ?: throw EmployeeNotExistsWithId(employeeId.toString())
    }

    override suspend fun TransactionalScope.updateEmployeeData(
        employeeId: EmployeeId,
        data: EmployeeDataEntity,
    ): EmployeeEntity {
        throwIfEmployeeNotExists(employeeId)
        EmployeeDataTable.insertEntity(name = data.name,
            description = data.description,
            gender = data.gender,
            employeeId = employeeId.id)
        return findEmployeeById(employeeId)
    }

    override suspend fun TransactionalScope.updateEmployeeLayoff(
        employeeId: EmployeeId,
        data: LayoffEmployeeRequestData,
    ): EmployeeEntity {
        throwIfEmployeeNotExists(employeeId)
        EmployeeLayoffTable.insertEntity(note = data.reason, employeeId = employeeId.id)
        return findEmployeeById(employeeId)
    }

    override suspend fun TransactionalScope.insertEmployee(
        request: CreateEmployeeRequestData,
        storageId: EmployeeStorageId,
    ): EmployeeEntity {
        val currentDate = Clock.System.now().toLocalDateTime(TimeZone.UTC)
        val employeeId = EmployeeTable.insertEntityAndGetId(creationDate = currentDate)
        listOf(transactionAsync {
            EmployeeToStorageTable.insertEntity(employeeId = employeeId, storageId = storageId.id)
        }, transactionAsync {
            EmployeeDataTable.insertEntity(
                name = request.name,
                description = request.description,
                gender = request.gender,
                employeeId = employeeId,
            )
        }).awaitAll()
        return EmployeeEntity(creationDate = currentDate, id = employeeId)

    }


    override suspend fun TransactionalScope.connectEmployeeToStorage(
        employeeId: EmployeeId,
        storageId: EmployeeStorageId,
    ): EmployeeEntity {
        EmployeeToStorageTable.insertEntity(employeeId = employeeId.id, storageId = storageId.id)
        return findEmployeeById(employeeId)
    }

    override fun findAllEmployeesData(employees: List<EmployeeEntity>): Map<Long, EmployeeDataEntity> {
        return EmployeeDataTable.select { EmployeeDataTable.employeeId inList employees.map { it.id } }
            .associate {
                val entity = it.toEmployeeDataEntity()
                entity.employeeId to entity
            }
    }

    override fun findAllEmployeesLayoff(employees: List<EmployeeEntity>): Map<Long, EmployeeLayoffEntity> {
        return EmployeeLayoffTable.select { EmployeeDataTable.employeeId inList employees.map { it.id } }
            .associate {
                val entity = it.toEmployeeLayoffEntity()
                entity.employeeId to entity
            }
    }

    override fun findAllEmployeesImage(employees: List<EmployeeEntity>): Map<Long, AssetEntity> {
        TODO("Not yet implemented")
    }

    override suspend fun TransactionalScope.findAllEmployees(
        paged: PagedData,
        storageId: EmployeeStorageId,
    ): List<EmployeeEntity> {
        val employees = EmployeeToStorageTable.select { EmployeeToStorageTable.storageId eq storageId.id }
            .map { it[EmployeeToStorageTable.employeeId] }
        return employees.map {
            transactionAsync {
                findEmployeeById(EmployeeId(it.value))
            }
        }.awaitAll()
    }

}