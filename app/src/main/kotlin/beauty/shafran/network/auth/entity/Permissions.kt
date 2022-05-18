package beauty.shafran.network.auth.entity

import beauty.shafran.network.account.data.AccountId
import beauty.shafran.network.account.entity.AccountTable
import beauty.shafran.network.utils.LongIdWithMetaTable
import org.jetbrains.exposed.sql.ReferenceOption
import ru.sulgik.exposed.TableToCreation

@TableToCreation
object PermissionTable : LongIdWithMetaTable("permission") {
    val label = varchar("title", 64)
}

@TableToCreation
object PermissionToAccountTable : LongIdWithMetaTable("permission_for_account") {
    val permissionId = reference("permission", PermissionTable, onDelete = ReferenceOption.CASCADE)
    val accountId = reference("account", AccountTable)
}

@TableToCreation
object PermissionToSessionTable : LongIdWithMetaTable("permission_for_session") {
    val permissionId = reference("permission", PermissionTable, onDelete = ReferenceOption.CASCADE)
    val sessionId = reference("sessionId", AccountSessionTable, onDelete = ReferenceOption.CASCADE)
}

interface IdPermission {
    val id: PermissionId
}

interface LabeledPermission {
    val label: PermissionLabel
}

abstract class ReferencedPermissions {

    abstract val permissions: List<PermissionEntity>
    val labels by lazy(mode = LazyThreadSafetyMode.PUBLICATION) { permissions.map { it.label } }
    val ids by lazy(mode = LazyThreadSafetyMode.PUBLICATION) { permissions.map { it.id } }

}

@JvmInline
value class PermissionId(
    val id: Long,
)

@JvmInline
value class PermissionLabel(
    val label: String,
)

data class PermissionEntity(
    override val label: PermissionLabel,
    override val id: PermissionId,
) : IdPermission, LabeledPermission

data class PermissionEntitiesToAccountEntity(
    val accountId: AccountId,
    override val permissions: List<PermissionEntity>,
) : ReferencedPermissions()

data class PermissionEntitiesToSessionEntity(
    val sessionId: AccountSessionId,
    override val permissions: List<PermissionEntity>,
) : ReferencedPermissions()