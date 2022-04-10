package beauty.shafran.network.di

//
//fun Application.configureKoin() {
//    install(KoinPlugin) {
//        slf4jLogger()
//        modules(
//            tokenDecoderModule,
//            converterModule,
//            preferencesModule,
//            mongoModule,
//            validatorsModule,
//            executorsModule,
//            reducers,
//        )
//    }
//}
//
//val mongoModule = module {
//    factory { get<Configuration>().buildMongoClientConfig() }
//    factory { get<Configuration>().buildAssetsConfig() }
//    factory { get<Configuration>().buildSecureConfig() }
//    single {
//
//    }
//    factoryOf(::MongoCustomersRepository) bind CustomersRepository::class
//    factoryOf(::MongoServicesRepository) bind ServicesRepository::class
//    factoryOf(::MongoEmployeesRepository) bind EmployeesRepository::class
//    factoryOf(::MongoSessionsRepository) bind SessionsRepository::class
//}
//
//val executorsModule = module {
//    factoryOf(::CustomersExecutorImpl) bind CustomersExecutor::class
//    factoryOf(::ServicesExecutorImpl) bind ServicesExecutor::class
//    factoryOf(::EmployeesExecutorImpl) bind EmployeesExecutor::class
//    factoryOf(::SessionsExecutorImpl) bind SessionsExecutor::class
//}
//
//val preferencesModule = module {
//    factoryOf(::PreferencesConfiguration) bind Configuration::class
//}
//
//val tokenDecoderModule = module {
//    factoryOf(::CardTokenDecoderImpl) bind CardTokenDecoder::class
//    factoryOf(::CardTokenEncoderImpl) bind CardTokenEncoder::class
//}
//
//val converterModule = module {
//    factoryOf(::PhoneNumberConverterImpl) bind PhoneNumberConverter::class
//    factoryOf(::AssetsConverterImpl) bind AssetsConverter::class
//    factoryOf(::MongoCustomersConverter) bind CustomersConverter::class
//    factoryOf(::ServicesConverterImpl) bind ServicesConverter::class
//    factoryOf(::EmployeesConverterImpl) bind EmployeesConverter::class
//    factoryOf(::SessionsConverterImpl) bind SessionsConverter::class
//}
//
//val validatorsModule = module {
//    factoryOf(::EmployeesValidatorImpl) bind EmployeesValidator::class
//    factoryOf(::CustomersValidatorImpl) bind CustomersValidator::class
//    factoryOf(::ServicesValidatorImpl) bind ServicesValidator::class
//    factoryOf(::SessionsValidatorImpl) bind SessionsValidator::class
//}
//val reducers = module {
//    factoryOf(::CustomersReducerImpl) bind CustomersReducer::class
//    factoryOf(::ServicesReducerImpl) bind ServicesReducer::class
//    factoryOf(::SessionsReducerImpl) bind SessionsReducer::class
//    factoryOf(::EmployeesReducerImpl) bind EmployeesReducer::class
//}