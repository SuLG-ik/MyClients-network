package beauty.shafran.network.auth.entities

import beauty.shafran.network.accounts.entities.AccountEntity
import javax.persistence.*

@Table(name = "account_password")
@Entity
class AccountPasswordEntity(
    @ManyToOne(targetEntity = AccountEntity::class, fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id", referencedColumnName = "id")
    var account: AccountEntity,
    @Column(name = "password_hash", length = 70)
    var passwordHash: String,
    @Id
    @GeneratedValue
    var id: Long = 0,
)
