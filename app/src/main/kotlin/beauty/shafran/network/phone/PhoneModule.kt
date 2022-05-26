package beauty.shafran.network.phone

import beauty.shafran.network.phone.converters.PhoneNumberConverter
import beauty.shafran.network.phone.converters.PhoneNumberConverterImpl
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.bind
import org.koin.dsl.module

val PhoneModule = module {
    factoryOf(::PhoneNumberConverterImpl) bind PhoneNumberConverter::class
}