package beauty.shafran.network.cards.executor

import beauty.shafran.network.auth.data.AuthorizedAccount
import beauty.shafran.network.cards.converter.CardsConverter
import beauty.shafran.network.cards.data.*
import beauty.shafran.network.cards.repository.CardsRepository
import beauty.shafran.network.cards.token.CardTokenEncoder
import beauty.shafran.network.utils.Transactional
import beauty.shafran.network.utils.invoke


class CardsExecutorImpl(
    private val repository: CardsRepository,
    private val converter: CardsConverter,
    private val cardEncoder: CardTokenEncoder,
    private val transactional: Transactional,
) : CardsExecutor {

    override suspend fun getCardById(request: GetCardByIdRequest, account: AuthorizedAccount): GetCardByIdResponse {
        return transactional {
            val cardEntity = repository.findCardById(request.cardId)
            GetCardByIdResponse(
                card = converter.buildCard(cardEntity)
            )
        }
    }

    override suspend fun getCardByCustomerId(
        request: GetCardByCustomerIdRequest,
        account: AuthorizedAccount,
    ): GetCardByCustomerIdResponse {
        return transactional {
            val cardEntity = repository.findCardByCustomerId(request.customerId)
            GetCardByCustomerIdResponse(
                card = converter.buildCard(cardEntity)
            )
        }
    }

    override suspend fun createCards(request: CreateCardsRequest, account: AuthorizedAccount): CreateCardsResponse {
        return transactional {
            val cards = repository.insertCards(request.count, request.cardsStorageId)
            CreateCardsResponse(
                cards = cards.map { converter.buildCard(it) }
            )
        }
    }

    override suspend fun createCardForCustomer(
        request: CreateCardForCustomerRequest,
        account: AuthorizedAccount,
    ): CreateCardForCustomerResponse {
        return transactional {
            val card = repository.insertCard(request.cardsStorageId)
            repository.connectCardToCustomer(CardId(card.id), request.customerId)
            CreateCardForCustomerResponse(
                card = cardEncoder.encodeTokenByCard(card)
            )
        }
    }

}