package beauty.shafran.network.auth.entities

import beauty.shafran.network.accounts.entities.AccountEntity
import javax.persistence.*

@Entity
@Table(name = "account_refresh_token")
class AccountRefreshTokenEntity(
    @OneToOne(targetEntity = AccountEntity::class, fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id", referencedColumnName = "id")
    val account: AccountEntity,
    @GeneratedValue
    @Id
    val id: Long = 0,
)