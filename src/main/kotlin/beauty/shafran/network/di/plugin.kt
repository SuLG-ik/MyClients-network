package beauty.shafran.network.di

import beauty.shafran.network.assets.converter.AssetsConverter
import beauty.shafran.network.assets.converter.AssetsConverterImpl
import beauty.shafran.network.config.Configuration
import beauty.shafran.network.config.MongoClientConfig
import beauty.shafran.network.config.PreferencesConfiguration
import beauty.shafran.network.config.toSettings
import beauty.shafran.network.customers.CustomersReducer
import beauty.shafran.network.customers.CustomersReducerImpl
import beauty.shafran.network.customers.converters.*
import beauty.shafran.network.customers.executor.CustomersExecutor
import beauty.shafran.network.customers.executor.CustomersExecutorImpl
import beauty.shafran.network.customers.repository.CustomersRepository
import beauty.shafran.network.customers.repository.MongoCustomersRepository
import beauty.shafran.network.customers.validators.CustomersValidator
import beauty.shafran.network.customers.validators.CustomersValidatorImpl
import beauty.shafran.network.employees.EmployeesReducer
import beauty.shafran.network.employees.EmployeesReducerImpl
import beauty.shafran.network.employees.converters.EmployeesConverter
import beauty.shafran.network.employees.converters.EmployeesConverterImpl
import beauty.shafran.network.employees.executor.EmployeesExecutor
import beauty.shafran.network.employees.executor.EmployeesExecutorImpl
import beauty.shafran.network.employees.repository.EmployeesRepository
import beauty.shafran.network.employees.repository.MongoEmployeesRepository
import beauty.shafran.network.employees.validators.EmployeesValidator
import beauty.shafran.network.employees.validators.EmployeesValidatorImpl
import beauty.shafran.network.koin.KoinPlugin
import beauty.shafran.network.phone.converters.PhoneNumberConverter
import beauty.shafran.network.phone.converters.PhoneNumberConverterImpl
import beauty.shafran.network.services.ServicesReducer
import beauty.shafran.network.services.ServicesReducerImpl
import beauty.shafran.network.services.converter.ServicesConverter
import beauty.shafran.network.services.converter.ServicesConverterImpl
import beauty.shafran.network.services.executor.ServicesExecutor
import beauty.shafran.network.services.executor.ServicesExecutorImpl
import beauty.shafran.network.services.repository.MongoServicesRepository
import beauty.shafran.network.services.repository.ServicesRepository
import beauty.shafran.network.services.validators.ServicesValidator
import beauty.shafran.network.services.validators.ServicesValidatorImpl
import beauty.shafran.network.session.SessionsReducer
import beauty.shafran.network.session.SessionsReducerImpl
import beauty.shafran.network.session.converters.SessionsConverter
import beauty.shafran.network.session.converters.SessionsConverterImpl
import beauty.shafran.network.session.executor.SessionsExecutor
import beauty.shafran.network.session.executor.SessionsExecutorImpl
import beauty.shafran.network.session.repository.MongoSessionsRepository
import beauty.shafran.network.session.repository.SessionsRepository
import beauty.shafran.network.session.validators.SessionsValidator
import beauty.shafran.network.session.validators.SessionsValidatorImpl
import io.ktor.server.application.*
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.bind
import org.koin.dsl.module
import org.koin.logger.slf4jLogger
import org.litote.kmongo.coroutine.coroutine
import org.litote.kmongo.reactivestreams.KMongo

fun Application.configureKoin() {
    install(KoinPlugin) {
        slf4jLogger()
        modules(
            tokenDecoderModule,
            converterModule,
            preferencesModule,
            mongoModule,
            validatorsModule,
            executorsModule,
            reducers,
        )
    }
}

val mongoModule = module {
    factory { get<Configuration>().buildMongoClientConfig() }
    factory { get<Configuration>().buildAssetsConfig() }
    factory { get<Configuration>().buildSecureConfig() }
    single {
        KMongo.createClient(get<MongoClientConfig>().toSettings())
            .getDatabase(get<MongoClientConfig>().database)
            .coroutine
    }
    factoryOf(::MongoCustomersRepository) bind CustomersRepository::class
    factoryOf(::MongoServicesRepository) bind ServicesRepository::class
    factoryOf(::MongoEmployeesRepository) bind EmployeesRepository::class
    factoryOf(::MongoSessionsRepository) bind SessionsRepository::class
}

val executorsModule = module {
    factoryOf(::CustomersExecutorImpl) bind CustomersExecutor::class
    factoryOf(::ServicesExecutorImpl) bind ServicesExecutor::class
    factoryOf(::EmployeesExecutorImpl) bind EmployeesExecutor::class
    factoryOf(::SessionsExecutorImpl) bind SessionsExecutor::class
}

val preferencesModule = module {
    factoryOf(::PreferencesConfiguration) bind Configuration::class
}

val tokenDecoderModule = module {
    factoryOf(::CardTokenDecoderImpl) bind CardTokenDecoder::class
    factoryOf(::CardTokenEncoderImpl) bind CardTokenEncoder::class
}

val converterModule = module {
    factoryOf(::PhoneNumberConverterImpl) bind PhoneNumberConverter::class
    factoryOf(::AssetsConverterImpl) bind AssetsConverter::class
    factoryOf(::MongoCustomersConverter) bind CustomersConverter::class
    factoryOf(::ServicesConverterImpl) bind ServicesConverter::class
    factoryOf(::EmployeesConverterImpl) bind EmployeesConverter::class
    factoryOf(::SessionsConverterImpl) bind SessionsConverter::class
}

val validatorsModule = module {
    factoryOf(::EmployeesValidatorImpl) bind EmployeesValidator::class
    factoryOf(::CustomersValidatorImpl) bind CustomersValidator::class
    factoryOf(::ServicesValidatorImpl) bind ServicesValidator::class
    factoryOf(::SessionsValidatorImpl) bind SessionsValidator::class
}

val reducers = module {
    factoryOf(::CustomersReducerImpl) bind CustomersReducer::class
    factoryOf(::ServicesReducerImpl) bind ServicesReducer::class
    factoryOf(::SessionsReducerImpl) bind SessionsReducer::class
    factoryOf(::EmployeesReducerImpl) bind EmployeesReducer::class
}