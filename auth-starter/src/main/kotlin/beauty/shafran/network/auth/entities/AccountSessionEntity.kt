package beauty.shafran.network.auth.entities

import beauty.shafran.network.accounts.entities.AccountEntity
import javax.persistence.*

@Entity
@Table(name = "account_session")
class AccountSessionEntity(
    @ManyToOne(targetEntity = AccountEntity::class, fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id", referencedColumnName = "id")
    val account: AccountEntity,
    @Id
    @GeneratedValue
    val id: Long = 0,
    val isDeactivated: Boolean = false,
)