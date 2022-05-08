package beauty.shafran.network.customers.entity

import kotlinx.serialization.Contextual
import kotlinx.serialization.SerialName
import org.litote.kmongo.Id
import org.litote.kmongo.newId

class CardsStorage(
    @SerialName("_id")
    @Contextual
    val id: Id<CustomersStorage> = newId(),
) {

    companion object {
        const val collectionName = "cards_storage"
    }

}