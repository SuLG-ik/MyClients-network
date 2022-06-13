package beauty.shafran.network.accounts

import beauty.shafran.network.accounts.converter.AccountConverter
import beauty.shafran.network.accounts.converter.AccountConverterImpl
import beauty.shafran.network.accounts.repository.AccountRepository
import beauty.shafran.network.accounts.repository.AccountRepositoryImpl
import beauty.shafran.network.accounts.route.AccountsRouter
import beauty.shafran.network.accounts.route.AccountsRouterImpl
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val AccountsModule = module {
    singleOf(::AccountsRouterImpl) bind AccountsRouter::class
    singleOf(::AccountRepositoryImpl) bind AccountRepository::class
    singleOf(::AccountConverterImpl) bind AccountConverter::class
}