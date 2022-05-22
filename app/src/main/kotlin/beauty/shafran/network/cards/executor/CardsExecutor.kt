package beauty.shafran.network.cards.executor

import beauty.shafran.network.auth.data.AuthorizedAccount
import beauty.shafran.network.cards.data.*

interface CardsExecutor {

    suspend fun getCardById(request: GetCardByIdRequest, account: AuthorizedAccount): GetCardByIdResponse

    suspend fun getCardByCustomerId(request: GetCardByCustomerIdRequest, account: AuthorizedAccount): GetCardByCustomerIdResponse

    suspend fun createCards(request: CreateCardsRequest, account: AuthorizedAccount): CreateCardsResponse

    suspend fun createCardForCustomer(request: CreateCardForCustomerRequest, account: AuthorizedAccount): CreateCardForCustomerResponse

}