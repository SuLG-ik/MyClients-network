package beauty.shafran.network.sessions.route

import beauty.shafran.network.companies.repository.CompanyRepository
import beauty.shafran.network.companies.route.Company
import beauty.shafran.network.customers.repository.CustomerRepository
import beauty.shafran.network.customers.route.Customer
import beauty.shafran.network.employees.route.Employee
import beauty.shafran.network.services.repository.ServiceRepository
import beauty.shafran.network.services.route.Service
import beauty.shafran.network.sessions.repository.*
import org.springframework.data.repository.findByIdOrNull
import org.springframework.graphql.data.method.annotation.Argument
import org.springframework.graphql.data.method.annotation.QueryMapping
import org.springframework.graphql.data.method.annotation.SchemaMapping
import org.springframework.stereotype.Controller
import org.springframework.transaction.annotation.Transactional

class SessionQuery

class Session(
    val id: Long,
    val serviceId: Long,
    val customerId: Long,
    val companyId: Long,
)

class SessionData(
    val session: Session,
    val description: String?,
)

class SessionUsage(
    val id: Long,
    val sessionId: Long,
)

class SessionUsageData(
    val usage: SessionUsage,
    val description: String?,
)

@Controller
class SessionController(
    private val sessionRepository: SessionRepository,
    private val serviceRepository: ServiceRepository,
    private val customerRepository: CustomerRepository,
    private val companyRepository: CompanyRepository,
    private val sessionEmployeeRepository: SessionEmployeeRepository,
    private val sessionUsageEmployeeRepository: SessionUsageEmployeeRepository,
    private val sessionUsageRepository: SessionUsageRepository,
    private val sessionDataRepository: SessionDataRepository,
    private val sessionUsageDataRepository: SessionUsageDataRepository,
) {


    @QueryMapping
    fun sessions() = SessionQuery()

    @Transactional
    @SchemaMapping(typeName = "SessionsQuery")
    fun session(@Argument id: Long): Session {
        val session = sessionRepository.findByIdOrNull(id) ?: TODO()
        return Session(
            id = id,
            customerId = session.customer.id,
            companyId = session.company.id,
            serviceId = session.service.id,
        )
    }


    @Transactional
    @SchemaMapping
    fun employees(source: Session): List<Employee> {
        return sessionEmployeeRepository.findAllBySession(sessionRepository.getReferenceById(source.id)).map {
            Employee(
                id = it.employee.id,
                name = it.employee.name,
                username = it.employee.username,
                companyId = it.employee.company.id,
            )
        }
    }

    @Transactional
    @SchemaMapping
    fun service(source: Session): Service {
        val service = serviceRepository.findByIdOrNull(source.serviceId) ?: TODO()
        return Service(
            id = service.id,
            companyId = service.company.id
        )
    }

    @Transactional
    @SchemaMapping
    fun customer(source: Session): Customer {
        val customer = customerRepository.findByIdOrNull(source.customerId) ?: TODO()
        return Customer(
            id = customer.id,
            name = customer.name
        )
    }

    @Transactional
    @SchemaMapping
    fun company(source: Session): Company {
        val company = companyRepository.findByIdOrNull(source.companyId) ?: TODO()
        return Company(
            id = company.id,
            title = company.title,
            codename = company.codename,
        )
    }

    @Transactional
    @SchemaMapping
    fun usages(source: Session): List<SessionUsage> {
        return sessionUsageRepository.findAllBySession(sessionRepository.getReferenceById(source.id)).map {
            SessionUsage(
                id = it.id,
                sessionId = it.session.id
            )
        }
    }

    @Transactional
    @SchemaMapping
    fun session(source: SessionUsage): Session {
        val session = sessionRepository.findByIdOrNull(source.sessionId) ?: TODO()
        return Session(
            id = session.id,
            customerId = session.customer.id,
            companyId = session.company.id,
            serviceId = session.service.id,
        )
    }

    @Transactional
    @SchemaMapping
    fun employees(source: SessionUsage): List<Employee> {
        return sessionUsageEmployeeRepository.findAllByUsage(sessionUsageRepository.getReferenceById(source.id)).map {
            Employee(
                id = it.employee.id,
                name = it.employee.name,
                username = it.employee.username,
                companyId = it.employee.company.id,
            )
        }
    }

    @Transactional
    @SchemaMapping
    fun data(source: Session): SessionData {
        val data = sessionDataRepository.findBySession(sessionRepository.getReferenceById(source.id))
        return SessionData(
            session = source,
            description = data?.description
        )
    }


    @Transactional
    @SchemaMapping
    fun data(source: SessionUsage): SessionUsageData {
        val data = sessionUsageDataRepository.findByUsage(sessionUsageRepository.getReferenceById(source.id))
        return SessionUsageData(
            usage = source,
            description = data?.description
        )
    }


}