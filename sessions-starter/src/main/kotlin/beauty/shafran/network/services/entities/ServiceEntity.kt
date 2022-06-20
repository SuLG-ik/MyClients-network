package beauty.shafran.network.services.entities

import beauty.shafran.network.companies.entities.CompanyEntity
import beauty.shafran.network.companies.entities.CompanyPlacementEntity
import javax.persistence.*

@Table(name = "service")
@Entity
class ServiceEntity(
    @ManyToOne(targetEntity = CompanyEntity::class, fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id", referencedColumnName = "id")
    var company: CompanyEntity,
    @GeneratedValue
    @Id
    var id: Long = 0,
)

@Table(name = "service_data")
@Entity
class ServiceDataEntity(
    @Column(name = "title", length = 32)
    var title: String,
    @Lob
    @Column(name = "description")
    var description: String,
    @OneToOne(targetEntity = ServiceEntity::class, fetch = FetchType.LAZY)
    @JoinColumn(name = "service_id", referencedColumnName = "id")
    var service: ServiceEntity,
    @GeneratedValue
    @Id
    var id: Long = 0,
)

@Table(name = "service_to_placement")
@Entity
class ServiceToPlacementEntity(
    @ManyToOne(targetEntity = CompanyPlacementEntity::class, fetch = FetchType.LAZY)
    @JoinColumn(name = "placement_id", referencedColumnName = "id")
    var placement: CompanyPlacementEntity,
    @ManyToOne(targetEntity = ServiceEntity::class, fetch = FetchType.LAZY)
    @JoinColumn(name = "service_id", referencedColumnName = "id")
    var service: ServiceEntity,
    @GeneratedValue
    @Id
    var id: Long = 0,
)