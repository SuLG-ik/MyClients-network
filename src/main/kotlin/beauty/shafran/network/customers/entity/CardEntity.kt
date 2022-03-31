package beauty.shafran.network.customers.entity

import kotlinx.serialization.Contextual
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.litote.kmongo.Id
import org.litote.kmongo.newId


@Serializable
data class CardEntity(
    val customerId: String,
    @Contextual
    @SerialName("_id")
    val id: Id<CardEntity> = newId(),
)

val CardEntity.Companion.collectionName get() = "cards"