package beauty.shafran.network.employees.executor

import beauty.shafran.network.employees.converters.EmployeesConverter
import beauty.shafran.network.employees.data.*
import beauty.shafran.network.employees.entity.EmployeeDataEntity
import beauty.shafran.network.employees.repository.EmployeesRepository
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class EmployeesExecutorImpl() : EmployeesExecutor, KoinComponent {


    private val employeesConverter: EmployeesConverter by inject()
    private val employeesRepository: EmployeesRepository by inject()

    override suspend fun createEmployee(data: CreateEmployeeRequest): CreateEmployeeResponse {
        val employeeData = EmployeeDataEntity(
            name = data.name,
            description = data.description,
            gender = data.gender
        )
        val employee = employeesRepository.insertEmployee(employeeData)
        return with(employeesConverter) {
            CreateEmployeeResponse(employee.toData())
        }
    }

    override suspend fun getAllEmployees(data: GetAllEmployeesRequest): GetAllEmployeesResponse {
        val services = employeesRepository.findAllEmployees(data.offset, data.page)
        return GetAllEmployeesResponse(
            employees = services.map { with(employeesConverter) { it.toData() } }.toList()
        )
    }

    override suspend fun layoffEmployee(data: LayoffEmployeeRequest): LayoffEmployeeResponse {
        val employee =
            employeesRepository.updateEmployeeLayoff(data.employeeId, with(employeesConverter) { data.toNewEntity() })
        return LayoffEmployeeResponse(
            employee = with(employeesConverter) { employee.toData() }
        )
    }

    override suspend fun getEmployeeById(data: GetEmployeeByIdRequest): GetEmployeeByIdResponse {
        val employee = employeesRepository.findEmployeeById(data.employeeId)
        return GetEmployeeByIdResponse(
            employee = with(employeesConverter) { employee.toData() }
        )
    }

    override suspend fun addEmployeeImage(data: AddEmployeeImageRequest): AddEmployeeImageResponse {
        TODO()
    }
}
