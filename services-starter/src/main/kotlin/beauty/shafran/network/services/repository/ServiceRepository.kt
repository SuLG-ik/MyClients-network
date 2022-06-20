package beauty.shafran.network.services.repository

import beauty.shafran.network.companies.entities.CompanyEntity
import beauty.shafran.network.companies.entities.CompanyPlacementEntity
import beauty.shafran.network.services.entities.ServiceDataEntity
import beauty.shafran.network.services.entities.ServiceEntity
import beauty.shafran.network.services.entities.ServiceToPlacementEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.querydsl.QuerydslPredicateExecutor
import org.springframework.graphql.data.GraphQlRepository

@GraphQlRepository
interface ServiceRepository : JpaRepository<ServiceEntity, Long>, QuerydslPredicateExecutor<ServiceEntity> {

    fun findAllByCompany(company: CompanyEntity): List<ServiceEntity>

}

@GraphQlRepository
interface ServiceDataRepository : JpaRepository<ServiceDataEntity, Long>, QuerydslPredicateExecutor<ServiceDataEntity> {

    fun findByService(service: ServiceEntity): ServiceDataEntity

}

@GraphQlRepository
interface ServiceToPlacementRepository : JpaRepository<ServiceToPlacementEntity, Long>,
    QuerydslPredicateExecutor<ServiceToPlacementEntity> {

    fun findAllByService(service: ServiceEntity): List<ServiceToPlacementEntity>


    fun existsByServiceAndPlacement(service: ServiceEntity, placement: CompanyPlacementEntity): Boolean

}