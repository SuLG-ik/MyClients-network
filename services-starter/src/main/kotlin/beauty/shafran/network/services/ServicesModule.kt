package beauty.shafran.network.services

import beauty.shafran.network.services.converter.ServiceConverter
import beauty.shafran.network.services.converter.ServiceConverterImpl
import beauty.shafran.network.services.repository.ServiceRepository
import beauty.shafran.network.services.repository.ServiceRepositoryImpl
import beauty.shafran.network.services.route.ServicesRouter
import beauty.shafran.network.services.route.ServicesRouterImpl
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val ServicesModule = module {
    singleOf(::ServiceRepositoryImpl) bind ServiceRepository::class
    singleOf(::ServiceConverterImpl) bind ServiceConverter::class
    singleOf(::ServicesRouterImpl) bind ServicesRouter::class
}