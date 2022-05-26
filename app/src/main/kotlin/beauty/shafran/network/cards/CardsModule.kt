package beauty.shafran.network.cards

import beauty.shafran.network.cards.converter.CardsConverter
import beauty.shafran.network.cards.converter.CardsConverterImpl
import beauty.shafran.network.cards.executor.CardsExecutor
import beauty.shafran.network.cards.executor.CardsExecutorImpl
import beauty.shafran.network.cards.repository.CardsRepository
import beauty.shafran.network.cards.repository.PostgresCardsRepository
import beauty.shafran.network.cards.token.*
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.bind
import org.koin.dsl.module

val CardsModule = module {
    factoryOf(::CardsExecutorImpl) bind CardsExecutor::class
    factoryOf(::PostgresCardsRepository) bind CardsRepository::class
    factoryOf(::CardsConverterImpl) bind CardsConverter::class
    factoryOf(::RandomCardTokenGenerator) bind CardTokenGenerator::class
    factoryOf(::CardTokenDecoderImpl) bind CardTokenDecoder::class
    factoryOf(::CardTokenEncoderImpl) bind CardTokenEncoder::class
}