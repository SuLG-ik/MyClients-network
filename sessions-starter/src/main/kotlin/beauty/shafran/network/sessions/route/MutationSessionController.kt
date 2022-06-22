package beauty.shafran.network.sessions.route

import beauty.shafran.network.customers.repository.CustomerRepository
import beauty.shafran.network.employees.repository.EmployeeRepository
import beauty.shafran.network.services.repository.ServiceRepository
import beauty.shafran.network.services.repository.TimesLimitedServiceRepository
import beauty.shafran.network.sessions.entities.*
import beauty.shafran.network.sessions.repository.*
import org.springframework.data.repository.findByIdOrNull
import org.springframework.graphql.data.method.annotation.Argument
import org.springframework.graphql.data.method.annotation.MutationMapping
import org.springframework.graphql.data.method.annotation.SchemaMapping
import org.springframework.stereotype.Controller
import org.springframework.transaction.annotation.Transactional
import javax.validation.Valid
import javax.validation.constraints.NotEmpty

class SessionsMutation

class AddSessionInput(
    val serviceId: Long,
    val customerId: Long,
    @field:NotEmpty
    val employeesIds: List<Long>,
    val description: String?,
)

class UseSessionInput(
    val sessionId: Long,
    @field:NotEmpty
    val employeesIds: List<Long>,
    val description: String?,
)


@Controller
class MutationSessionController(
    private val sessionRepository: SessionRepository,
    private val employeeRepository: EmployeeRepository,
    private val serviceRepository: ServiceRepository,
    private val customerRepository: CustomerRepository,
    private val sessionEmployeeRepository: SessionEmployeeRepository,
    private val sessionUsageEmployeeRepository: SessionUsageEmployeeRepository,
    private val sessionUsageRepository: SessionUsageRepository,
    private val sessionDataRepository: SessionDataRepository,
    private val sessionUsageDataRepository: SessionUsageDataRepository,
    private val timesLimitedServiceRepository: TimesLimitedServiceRepository,
) {

    @MutationMapping
    fun sessions() = SessionsMutation()

    @Transactional
    @SchemaMapping(typeName = "SessionsMutation")
    fun addSession(@Argument @Valid input: AddSessionInput): Session {
        val service = serviceRepository.findByIdOrNull(input.serviceId) ?: TODO()
        val employees = employeeRepository.findAllById(input.employeesIds)
        if (!employees.all { it.company.id == service.company.id })
            TODO()
        val session = sessionRepository.save(
            SessionEntity(
                company = service.company,
                service = service,
                customer = customerRepository.getReferenceById(input.customerId)
            )
        )
        sessionEmployeeRepository.saveAll(employees.map { SessionEmployeeEntity(employee = it, session = session) })
        if (input.description != null) {
            sessionDataRepository.save(SessionDataEntity(session = session, description = input.description))
        }
        return Session(
            id = session.id,
            serviceId = input.serviceId,
            customerId = input.customerId,
            companyId = session.company.id,
        )
    }

    @Transactional
    @SchemaMapping(typeName = "SessionsMutation")
    fun useSession(@Argument @Valid input: UseSessionInput): SessionUsage {
        val session = sessionRepository.findByIdOrNull(input.sessionId) ?: TODO()
        val usages = sessionUsageRepository.countAllBySession(session)
        val maxTimes = timesLimitedServiceRepository.findByService(session.service) ?: TODO()
        if (usages >= maxTimes.times) TODO()
        val employees = employeeRepository.findAllById(input.employeesIds)
        if (!employees.all { it.company.id == session.service.company.id })
            TODO()
        val usage = sessionUsageRepository.save(SessionUsageEntity(session = session))
        if (input.description != null) {
            sessionUsageDataRepository.save(SessionUsageDataEntity(usage = usage, description = input.description))
        }
        sessionUsageEmployeeRepository.saveAll(employees.map {
            SessionUsageEmployeeEntity(
                employee = it,
                usage = usage
            )
        })
        return SessionUsage(
            id = usage.id,
            sessionId = session.id
        )
    }

}