package beauty.shafran.network.customers.repository

import beauty.shafran.network.companies.entities.CompanyEntity
import beauty.shafran.network.customers.entities.*
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.querydsl.QuerydslPredicateExecutor
import org.springframework.graphql.data.GraphQlRepository

@GraphQlRepository
interface CustomerRepository : JpaRepository<CustomerEntity, Long>, QuerydslPredicateExecutor<CustomerEntity>

@GraphQlRepository
interface CustomerDataRepository : JpaRepository<CustomerDataEntity, Long>,
    QuerydslPredicateExecutor<CustomerDataEntity> {
    fun findByCustomer(customer: CustomerEntity): CustomerDataEntity?
}

@GraphQlRepository
interface CustomerCardRepository : JpaRepository<CustomerCardTokenEntity, Long>,
    QuerydslPredicateExecutor<CustomerCardTokenEntity> {

    fun findByCustomer(customer: CustomerEntity): CustomerCardTokenEntity?

    fun findByCard(card: CardEntity): CustomerCardTokenEntity?

}

@GraphQlRepository
interface CardRepository : JpaRepository<CardEntity, Long>,
    QuerydslPredicateExecutor<CardEntity> {

    fun findByToken(token: String): CardEntity?

}

interface CustomerToCompanyRepository : JpaRepository<CustomerToCompanyEntity, Long>,
    QuerydslPredicateExecutor<CustomerToCompanyEntity> {

        fun findAllByCompany(company: CompanyEntity): List<CustomerToCompanyEntity>

}