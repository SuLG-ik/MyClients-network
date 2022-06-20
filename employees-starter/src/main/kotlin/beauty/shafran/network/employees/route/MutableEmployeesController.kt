package beauty.shafran.network.employees.route

import beauty.shafran.network.companies.repository.CompanyPlacementRepository
import beauty.shafran.network.companies.repository.CompanyRepository
import beauty.shafran.network.employees.entities.EmployeeEntity
import beauty.shafran.network.employees.entities.EmployeeToPlacementEntity
import beauty.shafran.network.employees.repository.EmployeeRepository
import beauty.shafran.network.employees.repository.EmployeeToPlacementRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.graphql.data.method.annotation.Argument
import org.springframework.graphql.data.method.annotation.MutationMapping
import org.springframework.graphql.data.method.annotation.SchemaMapping
import org.springframework.stereotype.Controller
import org.springframework.transaction.annotation.Transactional


class EmployeesMutation

class AddEmployeeInput(
    val companyId: Long,
    val username: String,
    val name: String,
)

class AddEmployeeToPlacementInput(
    val employeeId: Long,
    val placementId: Long,
)


@Controller
class MutableEmployeesController(
    private val employeesRepository: EmployeeRepository,
    private val employeeToPlacementRepository: EmployeeToPlacementRepository,
    private val companyRepository: CompanyRepository,
    private val companyPlacementRepository: CompanyPlacementRepository,
) {

    @MutationMapping
    fun employees() = EmployeesMutation()

    @SchemaMapping(typeName = "EmployeesMutation")
    @Transactional
    fun addEmployee(@Argument input: AddEmployeeInput): Employee {
        val employee = employeesRepository.save(
            EmployeeEntity(
                company = companyRepository.getReferenceById(input.companyId),
                username = input.username,
                name = input.name,
            )
        )
        return Employee(
            id = employee.id,
            username = employee.username,
            name = employee.name,
            companyId = input.companyId
        )
    }

    @SchemaMapping(typeName = "EmployeesMutation")
    @Transactional
    fun addToPlacement(@Argument input: AddEmployeeToPlacementInput): Employee {
        val employee = employeesRepository.findByIdOrNull(input.employeeId) ?: TODO()
        val placement = companyPlacementRepository.getReferenceById(input.placementId)
        if (employeeToPlacementRepository.existsByEmployeeAndPlacement(
                employee,
                placement = placement
            )
        ) TODO()
        employeeToPlacementRepository.save(EmployeeToPlacementEntity(
            employee = employee,
            placement = placement
        ))
        return Employee(
            id = employee.id,
            username = employee.username,
            name = employee.name,
            companyId = employee.company.id
        )
    }

}