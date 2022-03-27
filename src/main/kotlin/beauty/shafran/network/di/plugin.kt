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
import beauty.shafran.network.customers.repository.CustomersRepository
import beauty.shafran.network.customers.repository.MongoCustomersRepository
import beauty.shafran.network.employees.EmployeesReducer
import beauty.shafran.network.employees.EmployeesReducerImpl
import beauty.shafran.network.employees.converters.EmployeesConverter
import beauty.shafran.network.employees.converters.EmployeesConverterImpl
import beauty.shafran.network.employees.repository.EmployeesRepository
import beauty.shafran.network.employees.repository.MongoEmployeesRepository
import beauty.shafran.network.phone.converters.PhoneNumberConverter
import beauty.shafran.network.phone.converters.PhoneNumberConverterImpl
import beauty.shafran.network.services.ServicesReducer
import beauty.shafran.network.services.ServicesReducerImpl
import beauty.shafran.network.services.converter.ServicesConverter
import beauty.shafran.network.services.converter.ServicesConverterImpl
import beauty.shafran.network.services.repository.MongoServicesRepository
import beauty.shafran.network.services.repository.ServicesRepository
import beauty.shafran.network.session.SessionsReducer
import beauty.shafran.network.session.SessionsReducerImpl
import beauty.shafran.network.session.converters.ServiceSessionsConverter
import beauty.shafran.network.session.converters.ServiceSessionsConverterImpl
import beauty.shafran.network.session.repository.MongoServiceSessionsRepository
import beauty.shafran.network.session.repository.ServiceSessionsRepository
import io.ktor.application.*
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.bind
import org.koin.dsl.module
import org.koin.ktor.ext.Koin
import org.litote.kmongo.coroutine.coroutine
import org.litote.kmongo.reactivestreams.KMongo
import java.util.logging.Level
import java.util.logging.Logger

fun Application.configureKoin() {
    install(Koin) {
        modules(
            tokenDecoderModule,
            converterModule,
            preferencesModule,
            mongoModule,
            reducers
        )
    }
}

val mongoModule = module {
    factory { get<Configuration>().buildMongoClientConfig() }
    factory { get<Configuration>().buildAssetsConfig() }
    single {
        KMongo.createClient(get<MongoClientConfig>().toSettings())
            .getDatabase(get<MongoClientConfig>().database)
            .coroutine.apply {
                Logger.getLogger("org.mongodb.driver").level = Level.OFF;
            }
    }
    factoryOf(::MongoCustomersRepository) bind CustomersRepository::class
    factoryOf(::MongoServicesRepository) bind ServicesRepository::class
    factoryOf(::MongoServiceSessionsRepository) bind ServiceSessionsRepository::class
    factoryOf(::MongoEmployeesRepository) bind EmployeesRepository::class
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
    factoryOf(::ServiceSessionsConverterImpl) bind ServiceSessionsConverter::class
}

val reducers = module {
    factoryOf(::CustomersReducerImpl) bind CustomersReducer::class
    factoryOf(::ServicesReducerImpl) bind ServicesReducer::class
    factoryOf(::SessionsReducerImpl) bind SessionsReducer::class
    factoryOf(::EmployeesReducerImpl) bind EmployeesReducer::class
}