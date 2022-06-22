package beauty.shafran.network.sessions.repository

import beauty.shafran.network.sessions.entities.*
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.querydsl.QuerydslPredicateExecutor
import org.springframework.graphql.data.GraphQlRepository

@GraphQlRepository
interface SessionRepository : JpaRepository<SessionEntity, Long>, QuerydslPredicateExecutor<SessionEntity>

@GraphQlRepository
interface SessionDataRepository : JpaRepository<SessionDataEntity, Long>,
    QuerydslPredicateExecutor<SessionDataEntity> {

    fun findBySession(usage: SessionEntity): SessionDataEntity?

}

@GraphQlRepository
interface SessionEmployeeRepository : JpaRepository<SessionEmployeeEntity, Long>,
    QuerydslPredicateExecutor<SessionEmployeeEntity> {

    fun findAllBySession(session: SessionEntity): List<SessionEmployeeEntity>

}

@GraphQlRepository
interface SessionUsageRepository : JpaRepository<SessionUsageEntity, Long>,
    QuerydslPredicateExecutor<SessionUsageEntity> {

    fun findAllBySession(session: SessionEntity): List<SessionUsageEntity>

    fun countAllBySession(session: SessionEntity): Long

}

@GraphQlRepository
interface SessionUsageEmployeeRepository : JpaRepository<SessionUsageEmployeeEntity, Long>,
    QuerydslPredicateExecutor<SessionUsageEmployeeEntity> {
    fun findAllByUsage(usage: SessionUsageEntity): List<SessionUsageEmployeeEntity>
}

@GraphQlRepository
interface SessionUsageDataRepository : JpaRepository<SessionUsageDataEntity, Long>,
    QuerydslPredicateExecutor<SessionUsageDataEntity> {

    fun findByUsage(usage: SessionUsageEntity): SessionUsageDataEntity?

}