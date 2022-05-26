package beauty.shafran.network.employees.repository

import beauty.shafran.network.assets.entity.AssetEntity
import beauty.shafran.network.employees.data.CreateEmployeeRequestData
import beauty.shafran.network.employees.data.EmployeeId
import beauty.shafran.network.employees.data.EmployeeStorageId
import beauty.shafran.network.employees.data.LayoffEmployeeRequestData
import beauty.shafran.network.employees.entity.EmployeeDataEntity
import beauty.shafran.network.employees.entity.EmployeeEntity
import beauty.shafran.network.employees.entity.EmployeeLayoffEntity
import beauty.shafran.network.utils.PagedData
import beauty.shafran.network.utils.TransactionalScope

interface EmployeesRepository {

    context (TransactionalScope) suspend fun throwIfEmployeeNotExists(employeeId: EmployeeId)

    context (TransactionalScope) suspend fun isEmployeeExists(employeeId: EmployeeId): Boolean

    context (TransactionalScope) suspend fun findEmployeeById(
        employeeId: EmployeeId,
    ): EmployeeEntity

    context (TransactionalScope) suspend fun updateEmployeeData(employeeId: EmployeeId, data: EmployeeDataEntity): EmployeeEntity

    context (TransactionalScope) suspend fun updateEmployeeLayoff(
        employeeId: EmployeeId,
        data: LayoffEmployeeRequestData,
    ): EmployeeEntity

    context (TransactionalScope) suspend fun insertEmployee(
        request: CreateEmployeeRequestData, storageId: EmployeeStorageId,
    ): EmployeeEntity

    context (TransactionalScope) suspend fun connectEmployeeToStorage(
        employeeId: EmployeeId,
        storageId: EmployeeStorageId,
    ): EmployeeEntity

    context (TransactionalScope)  suspend fun findAllEmployees(
        paged: PagedData,
        storageId: EmployeeStorageId,
    ): List<EmployeeEntity>

    context (TransactionalScope) suspend fun findEmployeeDataById(employeeId: EmployeeId): EmployeeDataEntity

    context (TransactionalScope) suspend fun findEmployeeLayoffById(employeeId: EmployeeId): EmployeeLayoffEntity

    context (TransactionalScope) suspend fun findEmployeeImageById(
        employeeId: EmployeeId,
    ): AssetEntity

    context (TransactionalScope) suspend fun findAllEmployeesData(employees: List<EmployeeEntity>): Map<Long, EmployeeDataEntity>

    context (TransactionalScope) suspend fun findAllEmployeesLayoff(employees: List<EmployeeEntity>): Map<Long, EmployeeLayoffEntity>

    context (TransactionalScope) suspend fun findAllEmployeesImage(employees: List<EmployeeEntity>): Map<Long, AssetEntity>
}