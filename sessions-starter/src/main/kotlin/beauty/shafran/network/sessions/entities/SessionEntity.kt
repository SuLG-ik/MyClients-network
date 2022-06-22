package beauty.shafran.network.sessions.entities

import beauty.shafran.network.companies.entities.CompanyEntity
import beauty.shafran.network.customers.entities.CustomerEntity
import beauty.shafran.network.employees.entities.EmployeeEntity
import beauty.shafran.network.services.entities.ServiceEntity
import javax.persistence.*

@Table(name = "session")
@Entity
class SessionEntity(
    @ManyToOne(targetEntity = CompanyEntity::class, fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id", referencedColumnName = "id")
    var company: CompanyEntity,
    @ManyToOne(targetEntity = CustomerEntity::class, fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", referencedColumnName = "id")
    var customer: CustomerEntity,
    @ManyToOne(targetEntity = ServiceEntity::class, fetch = FetchType.LAZY)
    @JoinColumn(name = "service_id", referencedColumnName = "id")
    var service: ServiceEntity,
    @GeneratedValue
    @Id
    var id: Long = 0,
)

@Entity
@Table(name = "session_data")
class SessionDataEntity(
    @Lob
    var description: String,
    @ManyToOne(targetEntity = SessionEntity::class, fetch = FetchType.LAZY)
    @JoinColumn(name = "session_id", referencedColumnName = "id")
    var session: SessionEntity,
    @GeneratedValue
    @Id
    var id: Long = 0,
)

@Entity
@Table(name = "session_employee")
class SessionEmployeeEntity(
    @ManyToOne(targetEntity = EmployeeEntity::class, fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id", referencedColumnName = "id")
    var employee: EmployeeEntity,
    @ManyToOne(targetEntity = SessionEntity::class, fetch = FetchType.LAZY)
    @JoinColumn(name = "session_id", referencedColumnName = "id")
    var session: SessionEntity,
    @GeneratedValue
    @Id
    var id: Long = 0,
)
