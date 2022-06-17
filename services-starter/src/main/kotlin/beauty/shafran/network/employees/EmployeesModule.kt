package beauty.shafran.network.employees

import beauty.shafran.network.employees.converter.EmployeeConverter
import beauty.shafran.network.employees.converter.EmployeeConverterImpl
import beauty.shafran.network.employees.repository.EmployeeRepository
import beauty.shafran.network.employees.repository.EmployeeRepositoryImpl
import beauty.shafran.network.employees.route.EmployeesRouter
import beauty.shafran.network.employees.route.EmployeesRouterImpl
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val EmployeesModule = module {
    singleOf(::EmployeeRepositoryImpl) bind EmployeeRepository::class
    singleOf(::EmployeeConverterImpl) bind EmployeeConverter::class
    singleOf(::EmployeesRouterImpl) bind EmployeesRouter::class
}