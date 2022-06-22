package beauty.shafran.network.customers.entities

import beauty.shafran.network.companies.entities.CompanyEntity
import javax.persistence.*

@Table(name = "customer")
@Entity
class CustomerEntity(
    @Column(length = 64)
    var name: String,
    @GeneratedValue
    @Id
    var id: Long = 0,
)

@Table(name = "customer_to_company")
@Entity
class CustomerToCompanyEntity(
    @ManyToOne(targetEntity = CompanyEntity::class, fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id", referencedColumnName = "id")
    var company: CompanyEntity,
    @ManyToOne(targetEntity = CustomerEntity::class)
    @JoinColumn(name = "customer_id", referencedColumnName = "id")
    var customer: CustomerEntity,
    @GeneratedValue
    @Id
    var id: Long = 0,
)

@Table(name = "customer_data")
@Entity
class CustomerDataEntity(
    @Lob
    var description: String,
    @OneToOne(targetEntity = CustomerEntity::class, fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", referencedColumnName = "id")
    var customer: CustomerEntity,
    @GeneratedValue
    @Id
    var id: Long = 0,
)

@Entity
@Table(name = "card")
class CardEntity(
    @ManyToOne(targetEntity = CompanyEntity::class, fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id", referencedColumnName = "id")
    var company: CompanyEntity,
    @Column(length = 24, unique = true)
    var token: String,
    @GeneratedValue
    @Id
    var id: Long = 0,
)


@Entity
@Table(name = "customer_card_token")
class CustomerCardTokenEntity(
    @OneToOne(targetEntity = CardEntity::class, fetch = FetchType.LAZY)
    @JoinColumn(name = "card_id", referencedColumnName = "id")
    var card: CardEntity,
    @OneToOne(targetEntity = CustomerEntity::class, fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", referencedColumnName = "id")
    var customer: CustomerEntity,
    @GeneratedValue
    @Id
    var id: Long = 0,
)