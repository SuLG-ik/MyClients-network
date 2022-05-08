package beauty.shafran.network.employees.repository

import beauty.shafran.EmployeeNotExistsWithId
import beauty.shafran.network.companies.entity.CompanyReferenceEntity
import beauty.shafran.network.employees.entity.EmployeeDataEntity
import beauty.shafran.network.employees.entity.EmployeeEntity
import beauty.shafran.network.employees.entity.EmployeeLayoffEntity
import beauty.shafran.network.employees.entity.collectionName
import beauty.shafran.network.utils.paged
import beauty.shafran.network.utils.toIdSecure
import org.koin.core.annotation.Single
import org.litote.kmongo.coroutine.CoroutineDatabase
import org.litote.kmongo.coroutine.updateOne
import org.litote.kmongo.div
import org.litote.kmongo.eq
import org.litote.kmongo.setTo

@Single
class MongoEmployeesRepository(
    coroutineDatabase: CoroutineDatabase,
) : EmployeesRepository {

    private val employeesCollection = coroutineDatabase.getCollection<EmployeeEntity>(EmployeeEntity.collectionName)
    override suspend fun throwIfEmployeeNotExists(employeeId: String) {
        if (!isEmployeeExists(employeeId))
            throw EmployeeNotExistsWithId(employeeId)
    }

    override suspend fun isEmployeeExists(employeeId: String): Boolean {
        return employeesCollection.countDocuments(EmployeeEntity::id eq employeeId.toIdSecure("employeeId")) >= 1
    }

    override suspend fun findEmployeeById(employeeId: String): EmployeeEntity {
        return employeesCollection.findOneById(employeeId.toIdSecure("employeeId"))
            ?: throw EmployeeNotExistsWithId(employeeId)
    }

    override suspend fun updateEmployeeData(employeeId: String, data: EmployeeDataEntity): EmployeeEntity {
        val employee = findEmployeeById(employeeId).copy(data = data)
        employeesCollection.updateOne(employee)
        return employee
    }

    override suspend fun updateEmployeeLayoff(employeeId: String, data: EmployeeLayoffEntity): EmployeeEntity {
        val employee = findEmployeeById(employeeId).copy(layoff = data)
        employeesCollection.updateOneById(
            employeeId.toIdSecure("employeeId"),
            EmployeeEntity::layoff setTo data
        )
        employeesCollection.updateOne(employee)
        return employee
    }

    override suspend fun insertEmployee(data: EmployeeDataEntity, companyId: String): EmployeeEntity {
        val employee = EmployeeEntity(data = data, companyReference = CompanyReferenceEntity(companyId))
        employeesCollection.insertOne(employee)
        return employee
    }

    override suspend fun findAllEmployees(offset: Int, page: Int, companyId: String): List<EmployeeEntity> {
        return employeesCollection.find(EmployeeEntity::companyReference eq CompanyReferenceEntity(companyId))
            .ascendingSort(EmployeeEntity::data / EmployeeDataEntity::name)
            .paged(offset, page)
            .toList()
    }

}