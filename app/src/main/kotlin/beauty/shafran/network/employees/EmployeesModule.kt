package beauty.shafran.network.employees

import beauty.shafran.network.employees.converters.EmployeesConverter
import beauty.shafran.network.employees.converters.EmployeesConverterImpl
import beauty.shafran.network.employees.executor.EmployeesExecutor
import beauty.shafran.network.employees.executor.EmployeesExecutorImpl
import beauty.shafran.network.employees.repository.EmployeesRepository
import beauty.shafran.network.employees.repository.PostgresEmployeesRepository
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.bind
import org.koin.dsl.module

val EmployeesModule = module {
    factoryOf(::EmployeesConverterImpl) bind EmployeesConverter::class
    factoryOf(::EmployeesExecutorImpl) bind EmployeesExecutor::class
    factoryOf(::PostgresEmployeesRepository) bind EmployeesRepository::class
}