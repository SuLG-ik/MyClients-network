package beauty.shafran.network.auth.tables

import beauty.shafran.network.accounts.tables.AccountTable
import beauty.shafran.network.exposed.LongIdWithMetaTable
import ru.sulgik.exposed.PropertyOfEntity
import ru.sulgik.exposed.TableToCreation
import ru.sulgik.exposed.TableWithEntity

@TableToCreation
@TableWithEntity
object AccountPasswordTable : LongIdWithMetaTable(name = "account_password") {

    @PropertyOfEntity
    val accountId = reference("account_id", AccountTable)

    @PropertyOfEntity
    val passwordHash = varchar("password_hash", 70)

}
