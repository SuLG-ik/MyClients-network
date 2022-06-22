package beauty.shafran.network.sessions.entities

import beauty.shafran.network.employees.entities.EmployeeEntity
import javax.persistence.*

@Entity
@Table(name = "session_usage")
class SessionUsageEntity(
    @ManyToOne(targetEntity = SessionEntity::class, fetch = FetchType.LAZY)
    @JoinColumn(name = "session_id", referencedColumnName = "id")
    var session: SessionEntity,
    @GeneratedValue
    @Id
    var id: Long = 0,
)

@Entity
@Table(name = "session_usage_data")
class SessionUsageDataEntity(
    @Lob
    var description: String,
    @ManyToOne(targetEntity = SessionUsageEntity::class, fetch = FetchType.LAZY)
    @JoinColumn(name = "usage_id", referencedColumnName = "id")
    var usage: SessionUsageEntity,
    @GeneratedValue
    @Id
    var id: Long = 0,
)

@Entity
@Table(name = "session_usage_employee")
class SessionUsageEmployeeEntity(
    @ManyToOne(targetEntity = EmployeeEntity::class, fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id", referencedColumnName = "id")
    var employee: EmployeeEntity,
    @ManyToOne(targetEntity = SessionUsageEntity::class, fetch = FetchType.LAZY)
    @JoinColumn(name = "usage_id", referencedColumnName = "id")
    var usage: SessionUsageEntity,
    @GeneratedValue
    @Id
    var id: Long = 0,
)
