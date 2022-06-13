package beauty.shafran.network.auth.tables

import beauty.shafran.network.accounts.tables.AccountTable
import beauty.shafran.network.exposed.LongIdWithMetaTable
import ru.sulgik.exposed.PropertyOfEntity
import ru.sulgik.exposed.TableToCreation
import ru.sulgik.exposed.TableWithEntity

@TableToCreation
@TableWithEntity
object AccountSessionTable : LongIdWithMetaTable(name = "account_session") {
    @PropertyOfEntity
    val accountId = reference("account_id", AccountTable)
}


@TableToCreation
@TableWithEntity
object AccountRefreshTokenTable : LongIdWithMetaTable(name = "account_refresh_token_table") {
    @PropertyOfEntity
    val accountId = reference("account_id", AccountTable)
}