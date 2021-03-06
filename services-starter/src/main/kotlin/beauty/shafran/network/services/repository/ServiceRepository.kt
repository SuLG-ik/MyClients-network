package beauty.shafran.network.services.repository

import beauty.shafran.network.companies.entities.CompanyEntity
import beauty.shafran.network.companies.entities.CompanyPlacementEntity
import beauty.shafran.network.services.entities.*
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


@GraphQlRepository
interface TimesLimitedServiceRepository : JpaRepository<TimesLimitedServiceEntity, Long>,
    QuerydslPredicateExecutor<TimesLimitedServiceEntity> {

    fun findByService(service: ServiceEntity): TimesLimitedServiceEntity?

}


@GraphQlRepository
interface ServiceInfoRepository : JpaRepository<ServiceInfoEntity, Long>,
    QuerydslPredicateExecutor<ServiceInfoEntity> {

    fun findByService(service: ServiceEntity): ServiceInfoEntity?

}

