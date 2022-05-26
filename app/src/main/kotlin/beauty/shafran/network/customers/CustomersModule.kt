package beauty.shafran.network.customers

import beauty.shafran.network.customers.converters.CustomersConverter
import beauty.shafran.network.customers.converters.MongoCustomersConverter
import beauty.shafran.network.customers.executor.CustomersExecutor
import beauty.shafran.network.customers.executor.CustomersExecutorImpl
import beauty.shafran.network.customers.repository.CustomersRepository
import beauty.shafran.network.customers.repository.PostgresCustomersRepository
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.bind
import org.koin.dsl.module

val CustomersModule = module {
    factoryOf(::PostgresCustomersRepository) bind CustomersRepository::class
    factoryOf(::MongoCustomersConverter) bind CustomersConverter::class
    factoryOf(::CustomersExecutorImpl) bind CustomersExecutor::class
}