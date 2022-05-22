package beauty.shafran.network.cards.repository

import beauty.shafran.network.cards.data.CardId
import beauty.shafran.network.cards.data.CardToken
import beauty.shafran.network.cards.data.CardsStorageId
import beauty.shafran.network.cards.entity.CardEntity
import beauty.shafran.network.customers.data.CustomerId
import beauty.shafran.network.utils.TransactionalScope

interface CardsRepository {

    context(TransactionalScope) suspend fun insertCards(count: Int, storageId: CardsStorageId): List<CardEntity>
    context(TransactionalScope) suspend fun insertCard(storageId: CardsStorageId): CardEntity
    context(TransactionalScope) suspend fun findCardByToken(token: CardToken): CardEntity
    context(TransactionalScope) suspend fun findCardById(cardId: CardId): CardEntity
    context(TransactionalScope) suspend fun findCardByCustomerId(customerId: CustomerId): CardEntity
    context(TransactionalScope) suspend fun connectCardToCustomer(cardId: CardId, customerId: CustomerId)
    context(TransactionalScope) suspend fun findConnectedCustomerToCard(cardId: CardId): CustomerId
}