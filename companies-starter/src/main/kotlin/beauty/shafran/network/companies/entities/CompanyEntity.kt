package beauty.shafran.network.companies.entities

import beauty.shafran.network.accounts.entities.AccountEntity
import javax.persistence.*

@Table(name = "company")
@Entity
class CompanyEntity(
    @Column(name = "codename", length = 32, updatable = false)
    val codename: String,
    @Column(name = "title", length = 64)
    val title: String,
    @GeneratedValue
    @Id
    val id: Long = 0,
)

@Table(name = "company_owner")
@Entity
class CompanyOwnerEntity(
    @OneToOne(targetEntity = CompanyMemberEntity::class, fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", referencedColumnName = "id")
    var member: CompanyMemberEntity,
    @OneToOne(targetEntity = CompanyEntity::class, fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id", referencedColumnName = "id")
    var company: CompanyEntity,
    @GeneratedValue
    @Id
    var id: Long = 0,
)

@Table(name = "company_member")
@Entity
class CompanyMemberEntity(
    @OneToOne(targetEntity = AccountEntity::class, fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id", referencedColumnName = "id")
    var account: AccountEntity,
    @OneToOne(targetEntity = CompanyEntity::class, fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id", referencedColumnName = "id")
    var company: CompanyEntity,
    @GeneratedValue
    @Id
    var id: Long = 0,
)