package beauty.shafran.network.customers.entity

import beauty.shafran.network.companies.entity.CompanyReferenceEntity
import kotlinx.serialization.Contextual
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.jetbrains.exposed.dao.id.IntIdTable
import org.litote.kmongo.Id
import org.litote.kmongo.newId

object CardsTable : IntIdTable("cards") {
    val token = varchar("token", 32).uniqueIndex()
    val customerId = reference("customer", CustomersTable).nullable()
}

@Serializable
data class CardEntity(
    val companyReference: CompanyReferenceEntity,
    val customerId: String?,
    @Contextual
    @SerialName("_id")
    val id: Id<CardEntity> = newId(),
)

val CardEntity.Companion.collectionName get() = "cards"