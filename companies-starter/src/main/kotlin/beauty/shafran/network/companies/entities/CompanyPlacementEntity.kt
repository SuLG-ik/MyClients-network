package beauty.shafran.network.companies.entities

import javax.persistence.*

@Table(name = "company_placement")
@Entity
class CompanyPlacementEntity(
    @Column(name = "title", length = 64)
    var title: String,
    @Column(name = "codename", length = 32)
    var codename: String,
    @ManyToOne(targetEntity = CompanyEntity::class, fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id", referencedColumnName = "id")
    var company: CompanyEntity,
    @Id
    @GeneratedValue
    var id: Long = 0,
)

@Table(name = "company_placement_member")
@Entity
class CompanyPlacementMemberEntity(
    @ManyToOne(targetEntity = CompanyMemberEntity::class, fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", referencedColumnName = "id")
    var member: CompanyMemberEntity,
    @ManyToOne(targetEntity = CompanyPlacementEntity::class, fetch = FetchType.LAZY)
    @JoinColumn(name = "placemen_id", referencedColumnName = "id")
    var placement: CompanyPlacementEntity,
    @Id
    @GeneratedValue
    var id: Long = 0,
)