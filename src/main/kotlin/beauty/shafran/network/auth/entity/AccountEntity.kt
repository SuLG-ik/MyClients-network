package beauty.shafran.network.auth.entity

import kotlinx.serialization.Serializable
import org.litote.kmongo.Id
import org.litote.kmongo.newId

@Serializable
class AccountEntity(
    val login: String,
    val password: String,
    val pairedBusiness: List<String> = listOf(),
    val id: Id<AccountEntity> = newId(),
)
