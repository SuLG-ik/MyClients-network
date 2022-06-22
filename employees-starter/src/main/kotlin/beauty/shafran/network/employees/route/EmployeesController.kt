package beauty.shafran.network.employees.route

import beauty.shafran.network.companies.repository.CompanyRepository
import beauty.shafran.network.companies.route.Company
import beauty.shafran.network.companies.route.CompanyPlacement
import beauty.shafran.network.employees.repository.EmployeeDataRepository
import beauty.shafran.network.employees.repository.EmployeeRepository
import beauty.shafran.network.employees.repository.EmployeeToPlacementRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.graphql.data.method.annotation.SchemaMapping
import org.springframework.stereotype.Controller
import org.springframework.transaction.annotation.Transactional

class Employee(
    val id: Long,
    val username: String,
    val name: String,
    val companyId: Long,
)

class EmployeeData(
    val employee: Employee,
)

@Controller
class EmployeesController(
    private val companyRepository: CompanyRepository,
    private val employeeRepository: EmployeeRepository,
    private val employeeDataRepository: EmployeeDataRepository,
    private val employeeToPlacementRepository: EmployeeToPlacementRepository,
) {


    @Transactional
    @SchemaMapping
    fun employees(source: Company): List<Employee> {
        val employees = employeeRepository.findAllByCompany(companyRepository.getReferenceById(source.id))
        return employees.map {
            Employee(
                id = it.id,
                username = it.username,
                name = it.name,
                companyId = source.id
            )
        }
    }

    @SchemaMapping
    fun data(source: Employee): EmployeeData {
        return EmployeeData(source)
    }

    @SchemaMapping
    fun name(source: EmployeeData): String {
        return source.employee.name
    }

    @Transactional
    @SchemaMapping
    fun description(source: EmployeeData): String? {
        return employeeDataRepository.findByEmployee(employeeRepository.getReferenceById(source.employee.id))?.description
    }

    @Transactional
    @SchemaMapping
    fun company(source: Employee): Company {
        val company = companyRepository.findByIdOrNull(source.companyId) ?: TODO()
        return Company(
            id = company.id,
            codename = company.codename,
            title = company.title
        )
    }

    @Transactional
    @SchemaMapping
    fun placements(source: Employee): List<CompanyPlacement> {
        val placements = employeeToPlacementRepository.findAllByEmployee(employeeRepository.getReferenceById(source.id))
        return placements.map {
            CompanyPlacement(
                id = it.placement.id,
                codename = it.placement.codename,
                companyId = it.placement.company.id,
                title = it.placement.title
            )
        }
    }

}