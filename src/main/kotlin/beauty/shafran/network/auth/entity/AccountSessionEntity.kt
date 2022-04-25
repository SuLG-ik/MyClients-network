package beauty.shafran.network.auth.entity

import kotlinx.serialization.Contextual
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.litote.kmongo.Id
import org.litote.kmongo.newId

@Serializable
class AccountSessionEntity(
    val accountId: String,
    val deviceType: DeviceType = DeviceType.UNKNOWN,
    val deactivation: AccountSessionDeactivation? = null,
    @SerialName("_id")
    @Contextual
    val id: Id<AccountSessionEntity> = newId(),
)

val AccountSessionEntity.Companion.collectionName get() = "account_sessions"

@Serializable
class AccountSessionDeactivation(
    val sourceSessionId: String,
)


enum class DeviceType {
    WINDOWS, MAC, BROWSER, ANDROID, IOS, UNKNOWN
}