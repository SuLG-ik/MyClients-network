package beauty.shafran.network.auth.repository

import beauty.shafran.network.accounts.entities.AccountEntity
import beauty.shafran.network.auth.entities.AccountPasswordEntity
import beauty.shafran.network.auth.entities.AccountRefreshTokenEntity
import beauty.shafran.network.auth.entities.AccountSessionEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query

interface AccountSessionsRepository : JpaRepository<AccountSessionEntity, Long> {

    @Modifying
    @Query("update AccountSessionEntity session set session.isDeactivated = true where session.account = :account")
    fun deactivateAccountSessions(account: AccountEntity)

}

interface AccountRefreshTokensRepository : JpaRepository<AccountRefreshTokenEntity, Long> {

    fun deleteAllByAccount(account: AccountEntity)

    fun findByAccount(account: AccountEntity): AccountRefreshTokenEntity?

}

interface AccountPasswordRepository : JpaRepository<AccountPasswordEntity, Long> {


    fun findByAccount(account: AccountEntity): AccountPasswordEntity?

}