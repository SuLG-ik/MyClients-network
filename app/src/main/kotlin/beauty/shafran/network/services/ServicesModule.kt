package beauty.shafran.network.services

import beauty.shafran.network.services.converter.ServicesConverter
import beauty.shafran.network.services.converter.ServicesConverterImpl
import beauty.shafran.network.services.executor.ServicesExecutor
import beauty.shafran.network.services.executor.ServicesExecutorImpl
import beauty.shafran.network.services.repository.PostgresServicesRepository
import beauty.shafran.network.services.repository.ServicesRepository
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.bind
import org.koin.dsl.module

val ServicesModule  = module{
    factoryOf(::PostgresServicesRepository) bind  ServicesRepository::class
    factoryOf(::ServicesExecutorImpl) bind ServicesExecutor::class
    factoryOf(::ServicesConverterImpl) bind ServicesConverter::class
}