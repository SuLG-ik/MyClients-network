package beauty.shafran.network.account.entity

import beauty.shafran.network.utils.LongIdWithMetaTable
import ru.sulgik.exposed.PropertyOfEntity
import ru.sulgik.exposed.TableToCreation
import ru.sulgik.exposed.TableWithEntity

@TableToCreation
@TableWithEntity
object AccountTable : LongIdWithMetaTable("account") {

    @PropertyOfEntity
    val username = varchar("username", 32)

}

@TableToCreation
@TableWithEntity
object AccountDataTable : LongIdWithMetaTable("account_data") {

    @PropertyOfEntity
    val name = varchar("name", 255)

    @PropertyOfEntity
    val accountId = reference("account", AccountTable)

}

@TableToCreation
@TableWithEntity
object AccountPasswordCredentialTable : LongIdWithMetaTable("account_password_credential") {

    @PropertyOfEntity
    val password = varchar("password", 128)

    @PropertyOfEntity
    val accountId = reference("account", AccountTable)

}
