package beauty.shafran.network.accounts.repositories

import beauty.shafran.network.accounts.entities.AccountDataEntity
import beauty.shafran.network.accounts.entities.AccountEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.data.jpa.repository.Query
import org.springframework.data.querydsl.QuerydslPredicateExecutor
import org.springframework.graphql.data.GraphQlRepository

@GraphQlRepository
interface AccountRepository : JpaRepository<AccountEntity, Long>, QuerydslPredicateExecutor<AccountEntity> {

    fun findByUsername(username: String): AccountEntity?
    fun existsByUsername(username: String):Boolean

}

@GraphQlRepository
interface AccountDataEntityRepository : JpaRepository<AccountDataEntity, Long>,
    QuerydslPredicateExecutor<AccountDataEntity> {

    fun findByAccount(account: AccountEntity): AccountDataEntity

}