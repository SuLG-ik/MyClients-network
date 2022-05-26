package beauty.shafran.network.companies

import beauty.shafran.network.companies.converter.CompanyConverter
import beauty.shafran.network.companies.converter.CompanyConverterImpl
import beauty.shafran.network.companies.executor.CompaniesExecutor
import beauty.shafran.network.companies.executor.CompaniesExecutorImpl
import beauty.shafran.network.companies.repository.CompaniesRepository
import beauty.shafran.network.companies.repository.PostgresCompaniesRepository
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.bind
import org.koin.dsl.module

val CompanyModule = module {
    factoryOf(::PostgresCompaniesRepository) bind CompaniesRepository::class
    factoryOf(::CompaniesExecutorImpl) bind CompaniesExecutor::class
    factoryOf(::CompanyConverterImpl) bind CompanyConverter::class
}