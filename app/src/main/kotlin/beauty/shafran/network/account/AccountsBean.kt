package beauty.shafran.network.account

import beauty.shafran.network.account.converter.AccountConverter
import beauty.shafran.network.account.converter.AccountConverterImpl
import beauty.shafran.network.account.executors.AccountsExecutor
import beauty.shafran.network.account.executors.AccountsExecutorImpl
import beauty.shafran.network.account.repository.AccountsRepository
import beauty.shafran.network.account.repository.AccountsRepositoryImpl
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.bind
import org.koin.dsl.module

val AccountsModule = module {
    factoryOf(::AccountsExecutorImpl) bind AccountsExecutor::class
    factoryOf(::AccountsRepositoryImpl) bind AccountsRepository::class
    factoryOf(::AccountConverterImpl) bind AccountConverter::class
}