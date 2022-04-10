package beauty.shafran.network.employees.repository

import beauty.shafran.network.EmployeeNotExistsWithId
import beauty.shafran.network.employees.entity.EmployeeDataEntity
import beauty.shafran.network.employees.entity.EmployeeEntity
import beauty.shafran.network.employees.entity.EmployeeLayoffEntity
import beauty.shafran.network.employees.entity.collectionName
import beauty.shafran.network.utils.paged
import beauty.shafran.network.utils.toIdSecure
import org.litote.kmongo.coroutine.CoroutineDatabase
import org.litote.kmongo.coroutine.updateOne
import org.litote.kmongo.div
import org.litote.kmongo.eq
import org.springframework.stereotype.Repository

@Repository
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
        return employeesCollection.findOneById(employeeId.toIdSecure<EmployeeEntity>("employeeId"))
            ?: throw EmployeeNotExistsWithId(employeeId)
    }

    override suspend fun updateEmployeeData(employeeId: String, data: EmployeeDataEntity): EmployeeEntity {
        val employee = findEmployeeById(employeeId).copy(data = data)
        employeesCollection.updateOne(employee)
        return employee
    }

    override suspend fun updateEmployeeLayoff(employeeId: String, data: EmployeeLayoffEntity): EmployeeEntity {
        val employee = findEmployeeById(employeeId).copy(layoff = data)
        employeesCollection.updateOne(employee)
        return employee
    }

    override suspend fun insertEmployee(data: EmployeeDataEntity): EmployeeEntity {
        val employee = EmployeeEntity(data = data)
        employeesCollection.insertOne(employee)
        return employee
    }

    override suspend fun findAllEmployees(offset: Int, page: Int): List<EmployeeEntity> {
        return employeesCollection.find()
            .ascendingSort(EmployeeEntity::data / EmployeeDataEntity::name)
            .paged(offset, page)
            .toList()
    }

}