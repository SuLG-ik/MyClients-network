package beauty.shafran.network.employees.validators

import beauty.shafran.network.employees.data.*
import beauty.shafran.network.validation.validateAndThrow
import jakarta.validation.Validator
import org.koin.core.component.KoinComponent

class EmployeesValidatorImpl(
    private val validator: Validator,
) : EmployeesValidator, KoinComponent {

    override suspend fun createEmployee(data: CreateEmployeeRequest): CreateEmployeeRequest {
        return validator.validateAndThrow(data.trim())
    }

    override suspend fun getAllEmployees(data: GetAllEmployeesRequest): GetAllEmployeesRequest {
        return validator.validateAndThrow(data)
    }

    override suspend fun layoffEmployee(data: LayoffEmployeeRequest): LayoffEmployeeRequest {
        return validator.validateAndThrow(data.trim())
    }

    override suspend fun getEmployeeById(data: GetEmployeeByIdRequest): GetEmployeeByIdRequest {
        return validator.validateAndThrow(data)
    }


}