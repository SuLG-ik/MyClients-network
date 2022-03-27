package beauty.shafran.network.employees.repository

import beauty.shafran.network.assets.entity.AssetEntity
import beauty.shafran.network.employees.converters.EmployeesConverter
import beauty.shafran.network.employees.data.*
import beauty.shafran.network.employees.entity.EmployeeDataEntity
import beauty.shafran.network.employees.entity.EmployeeEntity
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList
import org.bson.types.ObjectId
import org.litote.kmongo.coroutine.CoroutineDatabase

class MongoEmployeesRepository(
    client: CoroutineDatabase,
    private val employeesConverter: EmployeesConverter,
) : EmployeesRepository {


    private val collection = client.getCollection<EmployeeEntity>("employees")

    override suspend fun addEmployee(data: CreateEmployeeRequest): CreateEmployeeResponse {
        val employee = EmployeeEntity(
            data = EmployeeDataEntity(
                name = data.name,
                description = data.description,
                gender = data.gender
            )
        )
        collection.save(employee)
        return with(employeesConverter) {
            CreateEmployeeResponse(employee.toData())
        }
    }

    override suspend fun getAllEmployees(data: GetAllEmployeesRequest): GetAllEmployeesResponse {
        val services = collection.find().toFlow()
        return GetAllEmployeesResponse(
            employees = services.map { with(employeesConverter) { it.toData() } }.toList()
        )
    }

    override suspend fun layoffEmployee(data: LayoffEmployeeRequest): LayoffEmployeeResponse {
        val employee = collection.findOneById(ObjectId(data.employeeId))
            ?: TODO("EmployeeDoesNotExistsException(data.employeeId)")
        val newEmployee = with(employeesConverter) { employee.copy(layoff = data.toNewEntity()) }
        collection.save(newEmployee)
        return LayoffEmployeeResponse(
            employee = with(employeesConverter) { newEmployee.toData() }
        )
    }

    override suspend fun getEmployeeById(data: GetEmployeeByIdRequest): GetEmployeeByIdResponse {
        val employee = collection.findOneById(ObjectId(data.employeeId))
            ?: TODO("EmployeeDoesNotExistsException(data.employeeId)")
        return GetEmployeeByIdResponse(
            employee = with(employeesConverter) { employee.toData() }
        )
    }

    override suspend fun addEmployeeImage(data: AddEmployeeImageRequest): AddEmployeeImageResponse {
        val employee =
            collection.findOneById(ObjectId(data.data.employeeId))
                ?: TODO("EmployeeDoesNotExistsException(data.data.employeeId)")

        val newEmployee = employee.copy(image = AssetEntity(type = data.data.type, hash = data.asset.hash))
        return AddEmployeeImageResponse(
            employee = with(employeesConverter) { newEmployee.toData() }
        )
    }
}
