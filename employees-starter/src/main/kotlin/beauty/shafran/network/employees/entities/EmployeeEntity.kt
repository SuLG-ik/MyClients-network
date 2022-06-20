package beauty.shafran.network.employees.entities

import beauty.shafran.network.companies.entities.CompanyEntity
import beauty.shafran.network.companies.entities.CompanyPlacementEntity
import javax.persistence.*


@Table(name = "employee")
@Entity
class EmployeeEntity(
    @Column(length = 32)
    var username: String,
    @Column(length = 64)
    var name: String,
    @ManyToOne(targetEntity = CompanyEntity::class, fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id", referencedColumnName = "id")
    var company: CompanyEntity,
    @Id
    @GeneratedValue
    var id: Long,
)

@Table(name = "employee_to_placement")
@Entity
class EmployeeToPlacementEntity(
    @ManyToOne(targetEntity = EmployeeEntity::class, fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id", referencedColumnName = "id")
    var employee: EmployeeEntity,
    @ManyToOne(targetEntity = CompanyPlacementEntity::class, fetch = FetchType.LAZY)
    @JoinColumn(name = "placements_id", referencedColumnName = "id")
    var placement: CompanyPlacementEntity,
    @Id
    @GeneratedValue
    var id: Long,
)

@Table(name = "employee_data")
@Entity
class EmployeeDataEntity(
    @OneToOne(targetEntity = EmployeeEntity::class, fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id", referencedColumnName = "id")
    var employee: EmployeeEntity,
    @Lob
    var description: String,
    @Id
    @GeneratedValue
    var id: Long,
)