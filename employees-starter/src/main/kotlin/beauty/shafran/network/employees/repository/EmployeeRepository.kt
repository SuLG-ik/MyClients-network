package beauty.shafran.network.employees.repository

import beauty.shafran.network.companies.entities.CompanyEntity
import beauty.shafran.network.companies.entities.CompanyPlacementEntity
import beauty.shafran.network.employees.entities.EmployeeDataEntity
import beauty.shafran.network.employees.entities.EmployeeEntity
import beauty.shafran.network.employees.entities.EmployeeToPlacementEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.querydsl.QuerydslPredicateExecutor
import org.springframework.graphql.data.GraphQlRepository

@GraphQlRepository
interface EmployeeRepository : JpaRepository<EmployeeEntity, Long>, QuerydslPredicateExecutor<EmployeeEntity> {

    fun findAllByCompany(company: CompanyEntity): List<EmployeeEntity>

}

@GraphQlRepository
interface EmployeeDataRepository : JpaRepository<EmployeeDataEntity, Long>,
    QuerydslPredicateExecutor<EmployeeDataEntity> {

    fun findByEmployee(employee: EmployeeEntity): EmployeeDataEntity?

}

@GraphQlRepository
interface EmployeeToPlacementRepository : JpaRepository<EmployeeToPlacementEntity, Long>,
    QuerydslPredicateExecutor<EmployeeToPlacementEntity> {

    fun findAllByEmployee(employee: EmployeeEntity): List<EmployeeToPlacementEntity>

    fun existsByEmployeeAndPlacement(employee: EmployeeEntity, placement: CompanyPlacementEntity): Boolean

}