package beauty.shafran.network.employees.executor

import beauty.shafran.network.employees.converters.EmployeesConverter
import beauty.shafran.network.employees.data.*
import beauty.shafran.network.employees.entity.EmployeeDataEntity
import beauty.shafran.network.employees.repository.EmployeesRepository
import org.springframework.stereotype.Service

@Service
class EmployeesExecutorImpl(
    private val employeesConverter: EmployeesConverter,
    private val employeesRepository: EmployeesRepository,
) : EmployeesExecutor {


    override suspend fun createEmployee(request: CreateEmployeeRequest): CreateEmployeeResponse {
        val employeeData = EmployeeDataEntity(
            name = request.data.name,
            description = request.data.description,
            gender = request.data.gender
        )
        val employee = employeesRepository.insertEmployee(employeeData)
        return with(employeesConverter) {
            CreateEmployeeResponse(employee.toData())
        }
    }

    override suspend fun getAllEmployees(request: GetAllEmployeesRequest): GetAllEmployeesResponse {
        val services = employeesRepository.findAllEmployees(request.offset, request.page)
        return GetAllEmployeesResponse(
            employees = services.map { with(employeesConverter) { it.toData() } }.toList(),
            offset = request.offset,
            page = request.page,
        )
    }

    override suspend fun layoffEmployee(request: LayoffEmployeeRequest): LayoffEmployeeResponse {
        val employee =
            employeesRepository.updateEmployeeLayoff(request.employeeId,
                with(employeesConverter) { request.toNewEntity() })
        return LayoffEmployeeResponse(
            employee = with(employeesConverter) { employee.toData() }
        )
    }

    override suspend fun getEmployeeById(request: GetEmployeeByIdRequest): GetEmployeeByIdResponse {
        val employee = employeesRepository.findEmployeeById(request.employeeId)
        return GetEmployeeByIdResponse(
            employee = with(employeesConverter) { employee.toData() }
        )
    }

    override suspend fun addEmployeeImage(request: AddEmployeeImageRequest): AddEmployeeImageResponse {
        TODO()
    }
}
