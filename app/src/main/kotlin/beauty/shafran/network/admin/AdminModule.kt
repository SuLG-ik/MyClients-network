package beauty.shafran.network.admin

import beauty.shafran.network.admin.account.executor.AdminAccountExecutor
import beauty.shafran.network.admin.account.executor.AdminAccountExecutorImpl
import beauty.shafran.network.admin.company.executor.AdminCompanyExecutor
import beauty.shafran.network.admin.company.executor.AdminCompanyExecutorImpl
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.bind
import org.koin.dsl.module

val AdminModule = module {
    factoryOf(::AdminCompanyExecutorImpl) bind AdminCompanyExecutor::class
    factoryOf(::AdminAccountExecutorImpl) bind AdminAccountExecutor::class
}