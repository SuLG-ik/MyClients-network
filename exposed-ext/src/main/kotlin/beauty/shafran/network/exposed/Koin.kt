package beauty.shafran.network.exposed

import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IdTable
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.kotlin.datetime.datetime
import ru.sulgik.exposed.IgnoreOnInsert
import ru.sulgik.exposed.PropertyOfEntity
import ru.sulgik.exposed.TableId

abstract class IntIdWithMetaTable(name: String, columnName: String) : IdTable<Int>(name) {

    @TableId
    @IgnoreOnInsert
    @PropertyOfEntity
    final override val id: Column<EntityID<Int>> = integer(columnName).autoIncrement().entityId()

    final override val primaryKey = PrimaryKey(id)

    @IgnoreOnInsert
    @PropertyOfEntity
    val createdAt = datetime("created_at").clientDefault { Clock.System.now().toLocalDateTime(TimeZone.UTC) }

}

abstract class LongIdWithMetaTable(name: String, columnName: String = "id") : IdTable<Long>(name) {

    @TableId
    @IgnoreOnInsert
    @PropertyOfEntity
    final override val id: Column<EntityID<Long>> = long(columnName).autoIncrement().entityId()

    final override val primaryKey = PrimaryKey(id)

    @IgnoreOnInsert
    @PropertyOfEntity
    val createdAt = datetime("created_at").clientDefault { Clock.System.now().toLocalDateTime(TimeZone.UTC) }

}