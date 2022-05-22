package beauty.shafran.network.utils

import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IdTable
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.kotlin.datetime.datetime
import ru.sulgik.exposed.DefaultOnInsert
import ru.sulgik.exposed.IgnoreOnInsert
import ru.sulgik.exposed.PropertyOfEntity
import ru.sulgik.exposed.TableId

abstract class LongIdWithMetaTable(name: String = "", columnName: String = "id") : IdTable<Long>(name) {

    @PropertyOfEntity
    @DefaultOnInsert
    val creationDate = datetime("creationDate").clientDefault { Clock.System.now().toLocalDateTime(TimeZone.UTC) }

    @IgnoreOnInsert
    @PropertyOfEntity
    @TableId
    final override val id: Column<EntityID<Long>> = long(columnName).autoIncrement().entityId()

    final override val primaryKey = PrimaryKey(id)

}

inline fun LongIdWithMetaTable.selectLatest(where: SqlExpressionBuilder.() -> Op<Boolean>) = select(where)
    .orderBy(creationDate, order = SortOrder.DESC)
    .limit(1)
    .firstOrNull()

fun LongIdWithMetaTable.selectLatest(id: Long) = select { this@selectLatest.id eq id }
    .orderBy(creationDate, order = SortOrder.DESC)
    .limit(1)
    .firstOrNull()
