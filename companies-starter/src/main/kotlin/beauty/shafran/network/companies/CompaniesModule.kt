package beauty.shafran.network.companies

import beauty.shafran.network.companies.converter.CompanyConverter
import beauty.shafran.network.companies.converter.CompanyConverterImpl
import beauty.shafran.network.companies.repository.CompanyPlacementRepository
import beauty.shafran.network.companies.repository.CompanyPlacementRepositoryImpl
import beauty.shafran.network.companies.repository.CompanyRepository
import beauty.shafran.network.companies.repository.CompanyRepositoryImpl
import beauty.shafran.network.companies.route.CompaniesRouter
import beauty.shafran.network.companies.route.CompaniesRouterImpl
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val CompaniesModule = module {
    singleOf(::CompanyRepositoryImpl) bind CompanyRepository::class
    singleOf(::CompanyConverterImpl) bind CompanyConverter::class
    singleOf(::CompaniesRouterImpl) bind CompaniesRouter::class
    singleOf(::CompanyPlacementRepositoryImpl) bind CompanyPlacementRepository::class
}