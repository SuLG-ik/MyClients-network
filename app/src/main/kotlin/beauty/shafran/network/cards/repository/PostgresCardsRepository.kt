package beauty.shafran.network.cards.repository

import beauty.shafran.CardNotExists
import beauty.shafran.CardNotExistsForCustomer
import beauty.shafran.network.cards.data.CardId
import beauty.shafran.network.cards.data.CardToken
import beauty.shafran.network.cards.data.CardsStorageId
import beauty.shafran.network.cards.entity.*
import beauty.shafran.network.cards.token.CardTokenGenerator
import beauty.shafran.network.customers.data.CustomerId
import beauty.shafran.network.customers.entity.CustomerToCardTable
import beauty.shafran.network.customers.entity.insertEntity
import beauty.shafran.network.customers.entity.toCustomerToCardEntity
import beauty.shafran.network.utils.TransactionalScope
import beauty.shafran.network.utils.selectLatest
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.jetbrains.exposed.sql.batchInsert


class PostgresCardsRepository(
    private val tokenGenerator: CardTokenGenerator,
    private val clock: Clock,
) : CardsRepository {


    context(TransactionalScope) override suspend fun insertCard(storageId: CardsStorageId): CardEntity {
        val token = tokenGenerator.generate()
        val currentTime = clock.now().toLocalDateTime(TimeZone.UTC)
        val cardId = CardTable.insertEntityAndGetId(token = token.token, creationDate = currentTime)
        return CardEntity(
            token = token.token,
            creationDate = currentTime,
            id = cardId,
        )
    }

    context(TransactionalScope) override suspend fun insertCards(
        count: Int,
        storageId: CardsStorageId,
    ): List<CardEntity> {
        val cards = CardTable.batchInsert(List(count) {}) {
            this[CardTable.token] = tokenGenerator.generate().token
        }.map { it.toCardEntity() }
        CardToStorageTable.batchInsert(cards) {
            this[CardToStorageTable.cardId] = it.id
            this[CardToStorageTable.storageId] = storageId.id
        }
        return cards
    }

    context(TransactionalScope) override suspend fun findCardByToken(token: CardToken): CardEntity {
        return CardTable.selectLatest { CardTable.token eq token.token }?.toCardEntity()
            ?: throw CardNotExists(token.toString())
    }

    context(TransactionalScope) override suspend fun findCardById(cardId: CardId): CardEntity {
        return CardTable.findById(cardId.id) ?: throw CardNotExists(cardId.toString())
    }

    context(TransactionalScope) override suspend fun findCardByCustomerId(customerId: CustomerId): CardEntity {
        val customerToCard =
            CustomerToCardTable.selectLatest { CustomerToCardTable.customerId eq customerId.id }
                ?.toCustomerToCardEntity() ?: throw CardNotExists(customerId.toString())
        return CardTable.findById(customerToCard.cardId) ?: throw CardNotExists(customerId.toString())
    }

    context (TransactionalScope) override suspend fun connectCardToCustomer(cardId: CardId, customerId: CustomerId) {
        CustomerToCardTable.insertEntity(customerId = customerId.id, cardId = cardId.id)
    }

    context(TransactionalScope) override suspend fun findConnectedCustomerToCard(cardId: CardId): CustomerId {
        val customerToCard =
            CustomerToCardTable.selectLatest { CustomerToCardTable.cardId eq cardId.id }?.toCustomerToCardEntity()
                ?: throw CardNotExistsForCustomer(cardId.toString())
        return CustomerId(customerToCard.customerId)
    }

}