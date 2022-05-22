package beauty.shafran.network.cards.entity

import beauty.shafran.network.utils.LongIdWithMetaTable
import ru.sulgik.exposed.PropertyOfEntity
import ru.sulgik.exposed.TableToCreation
import ru.sulgik.exposed.TableWithEntity

@TableToCreation
@TableWithEntity
object CardTable : LongIdWithMetaTable("card") {
    @PropertyOfEntity
    val token = varchar("token", 32).uniqueIndex()
}

@TableToCreation
@TableWithEntity
object CardToStorageTable : LongIdWithMetaTable("card_to_storage") {
    @PropertyOfEntity
    val storageId = reference("storage", CardStorageTable)
    @PropertyOfEntity
    val cardId = reference("card", CardTable)
}

