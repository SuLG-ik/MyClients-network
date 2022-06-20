package beauty.shafran.network.companies.repository

import beauty.shafran.network.accounts.entities.AccountEntity
import beauty.shafran.network.companies.entities.CompanyEntity
import beauty.shafran.network.companies.entities.CompanyMemberEntity
import beauty.shafran.network.companies.entities.CompanyOwnerEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.querydsl.QuerydslPredicateExecutor
import org.springframework.graphql.data.GraphQlRepository


@GraphQlRepository
interface CompanyRepository : JpaRepository<CompanyEntity, Long>, QuerydslPredicateExecutor<CompanyEntity> {

    fun findByCodename(codename: String): CompanyEntity?

}


@GraphQlRepository
interface CompanyOwnerRepository : JpaRepository<CompanyOwnerEntity, Long>,
    QuerydslPredicateExecutor<CompanyOwnerEntity> {

    fun findByCompany(company: CompanyEntity): CompanyOwnerEntity?

}

@GraphQlRepository
interface CompanyMemberRepository : JpaRepository<CompanyMemberEntity, Long>,
    QuerydslPredicateExecutor<CompanyMemberEntity> {

    fun findAllByCompany(company: CompanyEntity): List<CompanyMemberEntity>

    fun findByCompanyAndAccount(company: CompanyEntity, account: AccountEntity): CompanyMemberEntity

}