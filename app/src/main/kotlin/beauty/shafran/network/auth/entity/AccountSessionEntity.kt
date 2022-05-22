package beauty.shafran.network.auth.entity

import beauty.shafran.network.account.data.AccountId
import beauty.shafran.network.account.entity.AccountTable
import beauty.shafran.network.utils.LongIdWithMetaTable
import beauty.shafran.network.utils.customEnumeration
import kotlinx.serialization.Serializable
import ru.sulgik.exposed.TableToCreation

@TableToCreation
object AccountSessionTable : LongIdWithMetaTable("account_auth_session") {
    val accountId = reference("account", AccountTable)
    val deviceType = customEnumeration<DeviceType>("device_type", DeviceType.typeName)
}

class AccountSessionEntity(
    val accountId: AccountId,
    val deviceType: DeviceType = DeviceType.UNKNOWN,
    val deactivation: AccountSessionDeactivation? = null,
    val id: AccountSessionId,
)

@JvmInline
@Serializable
value class AccountSessionId(val id: Long)

@Serializable
class AccountSessionDeactivation(
    val sourceSessionId: String,
)


enum class DeviceType {
    WINDOWS, MAC, BROWSER, ANDROID, IOS, UNKNOWN;

    companion object
}

val DeviceType.Companion.typeName get() = "device_type"
