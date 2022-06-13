package beauty.shafran.network.accounts.tables

import beauty.shafran.network.exposed.LongIdWithMetaTable
import ru.sulgik.exposed.PropertyOfEntity
import ru.sulgik.exposed.TableToCreation
import ru.sulgik.exposed.TableWithEntity

@TableToCreation
@TableWithEntity
object AccountTable : LongIdWithMetaTable("account") {

    @PropertyOfEntity
    val username = varchar("username", 64).uniqueIndex()

}

@TableToCreation
@TableWithEntity
object AccountDataTable : LongIdWithMetaTable("account_data") {

    @PropertyOfEntity
    val name = varchar("name", 128)

    @PropertyOfEntity
    val accountId = reference("account_id", AccountTable)

}