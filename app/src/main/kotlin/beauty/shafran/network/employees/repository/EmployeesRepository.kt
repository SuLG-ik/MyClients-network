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

    suspend fun TransactionalScope.throwIfEmployeeNotExists(employeeId: EmployeeId)

    suspend fun TransactionalScope.isEmployeeExists(employeeId: EmployeeId): Boolean

    suspend fun TransactionalScope.findEmployeeById(
        employeeId: EmployeeId,
    ): EmployeeEntity

    suspend fun TransactionalScope.updateEmployeeData(employeeId: EmployeeId, data: EmployeeDataEntity): EmployeeEntity

    suspend fun TransactionalScope.updateEmployeeLayoff(
        employeeId: EmployeeId,
        data: LayoffEmployeeRequestData,
    ): EmployeeEntity

    suspend fun TransactionalScope.insertEmployee(
        request: CreateEmployeeRequestData, storageId: EmployeeStorageId,
    ): EmployeeEntity

    suspend fun TransactionalScope.connectEmployeeToStorage(
        employeeId: EmployeeId,
        storageId: EmployeeStorageId,
    ): EmployeeEntity

    suspend fun TransactionalScope.findAllEmployees(
        paged: PagedData,
        storageId: EmployeeStorageId,
    ): List<EmployeeEntity>

    suspend fun TransactionalScope.findEmployeeDataById(employeeId: EmployeeId): EmployeeDataEntity

    suspend fun TransactionalScope.findEmployeeLayoffById(employeeId: EmployeeId): EmployeeLayoffEntity

    suspend fun TransactionalScope.findEmployeeImageById(
        employeeId: EmployeeId,
    ): AssetEntity

    fun findAllEmployeesData(employees: List<EmployeeEntity>): Map<Long, EmployeeDataEntity>

    fun findAllEmployeesLayoff(employees: List<EmployeeEntity>): Map<Long, EmployeeLayoffEntity>

    fun findAllEmployeesImage(employees: List<EmployeeEntity>): Map<Long, AssetEntity>
}