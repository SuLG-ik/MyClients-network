package beauty.shafran.network.accounts.entities

import javax.persistence.*

@Table(name = "account")
@Entity
class AccountEntity(
    @Column(name = "name", length = 16, unique = true, updatable = false)
    val username: String,
    @GeneratedValue
    @Id
    val id: Long = 0,
)


@Table(name = "account_data")
@Entity
class AccountDataEntity(
    @Column(name = "name", length = 64)
    val name: String,
    @OneToOne(targetEntity = AccountEntity::class, fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id", referencedColumnName = "id")
    val account: AccountEntity,
    @GeneratedValue
    @Id
    val id: Long = 0,
)