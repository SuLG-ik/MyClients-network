package beauty.shafran.network.employees.executor

import beauty.shafran.network.auth.AuthorizationValidator
import beauty.shafran.network.auth.data.AuthorizedAccount
import beauty.shafran.network.auth.throwIfNotAccessedForCompany
import beauty.shafran.network.companies.data.CompanyId
import beauty.shafran.network.companies.entity.AccessScope
import beauty.shafran.network.employees.converters.EmployeesConverter
import beauty.shafran.network.employees.data.*
import beauty.shafran.network.employees.entity.EmployeeDataEntity
import beauty.shafran.network.employees.entity.EmployeeLayoffEntity
import beauty.shafran.network.employees.repository.EmployeesRepository
import org.koin.core.annotation.Single

@Single
class EmployeesExecutorImpl(
    private val employeesConverter: EmployeesConverter,
    private val employeesRepository: EmployeesRepository,
    private val auth: AuthorizationValidator,
) : EmployeesExecutor {


    override suspend fun createEmployee(
        request: CreateEmployeeRequest,
        account: AuthorizedAccount,
    ): CreateEmployeeResponse {
        auth.throwIfNotAccessedForCompany(
            companyId = CompanyId(request.companyId),
            scope = AccessScope.EMPLOYEES_ADD,
            account = account
        )
        val employeeData = EmployeeDataEntity(
            name = request.data.name,
            description = request.data.description,
            gender = request.data.gender
        )
        val employee = employeesRepository.insertEmployee(employeeData, companyId = request.companyId)
        return with(employeesConverter) {
            CreateEmployeeResponse(employee.toData())
        }
    }

    override suspend fun getAllEmployees(
        request: GetAllEmployeesRequest,
        account: AuthorizedAccount,
    ): GetAllEmployeesResponse {
        auth.throwIfNotAccessedForCompany(request.companyId, AccessScope.EMPLOYEES_READ, account)
        val services =
            employeesRepository.findAllEmployees(request.offset, request.page, companyId = request.companyId.id)
        return GetAllEmployeesResponse(
            employees = services.map { with(employeesConverter) { it.toData() } }.toList(),
            offset = request.offset,
            page = request.page,
        )
    }

    override suspend fun layoffEmployee(
        request: LayoffEmployeeRequest,
        account: AuthorizedAccount,
    ): LayoffEmployeeResponse {
        val employee = employeesRepository.findEmployeeById(request.employeeId)
        auth.throwIfNotAccessedForCompany(companyId = CompanyId(employee.companyReference.companyId),
            scope = AccessScope.EMPLOYEES_REMOVE,
            account = account)
        val data = EmployeeLayoffEntity(
            reason = request.data.reason,
        )
        val newEmployee =
            employeesRepository.updateEmployeeLayoff(request.employeeId,
                data)
        return LayoffEmployeeResponse(
            employee = with(employeesConverter) { newEmployee.toData() }
        )
    }

    override suspend fun getEmployeeById(
        request: GetEmployeeWithIdRequest,
        account: AuthorizedAccount,
    ): GetEmployeeByIdResponse {
        val employee = employeesRepository.findEmployeeById(request.employeeId)
        auth.throwIfNotAccessedForCompany(companyId = CompanyId(employee.companyReference.companyId),
            scope = AccessScope.EMPLOYEES_READ,
            account = account)
        return GetEmployeeByIdResponse(
            employee = with(employeesConverter) { employee.toData() }
        )
    }

    override suspend fun addEmployeeImage(
        request: AddEmployeeImageRequest,
        account: AuthorizedAccount,
    ): AddEmployeeImageResponse {
        TODO()
    }
}
