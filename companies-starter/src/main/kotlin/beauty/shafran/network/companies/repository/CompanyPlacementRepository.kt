package beauty.shafran.network.companies.repository

import beauty.shafran.network.companies.entities.CompanyEntity
import beauty.shafran.network.companies.entities.CompanyPlacementEntity
import beauty.shafran.network.companies.entities.CompanyPlacementMemberEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.querydsl.QuerydslPredicateExecutor
import org.springframework.graphql.data.GraphQlRepository

@GraphQlRepository
interface CompanyPlacementRepository : JpaRepository<CompanyPlacementEntity, Long>,
    QuerydslPredicateExecutor<CompanyPlacementEntity> {

    fun findAllByCompany(company: CompanyEntity): List<CompanyPlacementEntity>

}

@GraphQlRepository
interface CompanyPlacementMemberRepository : JpaRepository<CompanyPlacementMemberEntity, Long>,
    QuerydslPredicateExecutor<CompanyPlacementMemberEntity> {

    fun findAllByPlacement(placement: CompanyPlacementEntity): List<CompanyPlacementMemberEntity>

}