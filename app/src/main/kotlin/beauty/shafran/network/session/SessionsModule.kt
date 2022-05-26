package beauty.shafran.network.session

import beauty.shafran.network.session.converters.SessionsConverter
import beauty.shafran.network.session.converters.SessionsConverterImpl
import beauty.shafran.network.session.executor.SessionsExecutor
import beauty.shafran.network.session.executor.SessionsExecutorImpl
import beauty.shafran.network.session.repository.PostgresServiceSessionsRepository
import beauty.shafran.network.session.repository.ServiceSessionsRepository
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.bind
import org.koin.dsl.module

val ServiceSessionsModule = module {
    factoryOf(::PostgresServiceSessionsRepository) bind ServiceSessionsRepository::class
    factoryOf(::SessionsExecutorImpl) bind SessionsExecutor::class
    factoryOf(::SessionsConverterImpl) bind SessionsConverter::class
}