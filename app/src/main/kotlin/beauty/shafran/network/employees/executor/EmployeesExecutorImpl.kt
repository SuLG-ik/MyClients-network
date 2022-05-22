package beauty.shafran.network.employees.executor

import beauty.shafran.network.auth.AuthorizationValidator
import beauty.shafran.network.auth.data.AuthorizedAccount
import beauty.shafran.network.employees.converters.EmployeesConverter
import beauty.shafran.network.employees.data.*
import beauty.shafran.network.employees.repository.EmployeesRepository
import beauty.shafran.network.utils.Transactional
import beauty.shafran.network.utils.TransactionalScope
import org.koin.core.annotation.Single

@Single
class EmployeesExecutorImpl(
    private val employeesConverter: EmployeesConverter,
    private val employeesRepository: EmployeesRepository,
    private val auth: AuthorizationValidator,
    private val transactional: Transactional,
) : EmployeesExecutor {


    override suspend fun createEmployee(
        request: CreateEmployeeRequest,
        account: AuthorizedAccount,
    ): CreateEmployeeResponse {
        return transactional.withSuspendedTransaction {
            val employee = employeesRepository.run { insertEmployee(request.data, storageId = request.companyId) }
            val data = employeesRepository.run { findEmployeeDataById(EmployeeId(employee.id)) }
            with(employeesConverter) {
                CreateEmployeeResponse(buildEmployee(employee, data, null, null))
            }
        }
    }

    override suspend fun getAllEmployees(
        request: GetAllEmployeesRequest,
        account: AuthorizedAccount,
    ): GetAllEmployeesResponse {
        return transactional.withSuspendedTransaction {
            val employees =
                with(employeesRepository) { findAllEmployees(paged = request.paged, storageId = request.storageId) }
            val dataDeferred = transactionAsync { employeesRepository.run { findAllEmployeesData(employees) } }
            val layoffDeferred = transactionAsync { employeesRepository.run { findAllEmployeesLayoff(employees) } }
            val assetsDeferred = transactionAsync { employeesRepository.run { findAllEmployeesImage(employees) } }
            val data = dataDeferred.await()
            val layoffs = layoffDeferred.await()
            val assets = assetsDeferred.await()
            GetAllEmployeesResponse(
                employees = employees.mapNotNull {
                    employeesConverter.buildEmployee(
                        employeeEntity = it,
                        employeeDataEntity = data.getOrElse(it.id) { return@mapNotNull null },
                        employeeLayoffEntity = layoffs[it.id],
                        image = assets[it.id]
                    )
                },
                paged = request.paged
            )
        }
    }

    override suspend fun layoffEmployee(
        request: LayoffEmployeeRequest,
        account: AuthorizedAccount,
    ): LayoffEmployeeResponse {
        return transactional.withSuspendedTransaction {
            employeesRepository.run { updateEmployeeLayoff(request.employeeId, request.data) }
            LayoffEmployeeResponse(
                employee = loadEmployee(request.employeeId)
            )
        }
    }

    private suspend fun TransactionalScope.loadEmployee(employeeId: EmployeeId): Employee {
        val employee = transactionAsync { employeesRepository.run { findEmployeeById(employeeId) } }
        val data = transactionAsync { employeesRepository.run { findEmployeeDataById(employeeId) } }
        val layoff = transactionAsync { employeesRepository.run { findEmployeeLayoffById(employeeId) } }
        val image = transactionAsync { employeesRepository.run { findEmployeeImageById(employeeId) } }
        return employeesConverter.buildEmployee(
            employeeEntity = employee.await(),
            employeeDataEntity = data.await(),
            employeeLayoffEntity = layoff.await(),
            image = image.await()
        )
    }

    override suspend fun getEmployeeById(
        request: GetEmployeeWithIdRequest,
        account: AuthorizedAccount,
    ): GetEmployeeByIdResponse {
        return transactional.withSuspendedTransaction {
            GetEmployeeByIdResponse(
                employee = loadEmployee(request.employeeId)
            )
        }
    }

    override suspend fun addEmployeeImage(
        request: AddEmployeeImageRequest,
        account: AuthorizedAccount,
    ): AddEmployeeImageResponse {
        TODO()
    }
}
