package beauty.shafran.network.account.entity

import beauty.shafran.network.assets.entity.AssetEntity
import beauty.shafran.network.utils.MetaEntity
import kotlinx.serialization.Contextual
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.litote.kmongo.Id
import org.litote.kmongo.newId

@Serializable
class AccountEntity(
    val data: AccountEntityData,
    val image: AssetEntity? = null,
    val meta: MetaEntity = MetaEntity(),
    @SerialName("_id")
    @Contextual
    val id: Id<AccountEntity> = newId(),
)

val AccountEntity.Companion.collectionName get() = "accounts"

enum class AccountDeactivation {
    UNKNOWN,
}

@Serializable
data class AccountDeactivationEntity(
    val accountId: String,
    val data: AccountDeactivationData,
    val disabling: AccountDeactivationDisablingData? = null,
    @SerialName("_id")
    @Contextual
    val id: Id<AccountDeactivationEntity> = newId(),
)

@Serializable
data class AccountDeactivationDisablingData(
    val reason: AccountDeactivation,
    val note: String,
    val meta: MetaEntity,
)

@Serializable
data class AccountDeactivationData(
    val reason: AccountDeactivation,
    val note: String,
    val meta: MetaEntity,
)

val AccountDeactivationEntity.Companion.collectionName get() = "accounts_deactivations"

@Serializable
data class AccountEntityData(
    val name: String,
    val username: String,
)

@Serializable
data class AccountPasswordAuthEntity(
    val password: String,
    val accountId: String,
    val meta: MetaEntity = MetaEntity(),
    @SerialName("_id")
    @Contextual
    val id: Id<AccountPasswordAuthEntity> = newId(),
)

val AccountPasswordAuthEntity.Companion.collectionName get() = "accounts_auth_password"