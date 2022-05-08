package beauty.shafran.network.utils

import beauty.shafran.network.customers.entity.CustomersDataTable
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.jetbrains.exposed.dao.id.LongIdTable
import org.jetbrains.exposed.sql.Op
import org.jetbrains.exposed.sql.SqlExpressionBuilder
import org.jetbrains.exposed.sql.kotlin.datetime.datetime
import org.jetbrains.exposed.sql.select

abstract class LongIdWithMetaTable(name: String = "", columnName: String = "id") : LongIdTable(name, columnName) {
    val creationDate = datetime("creationDate").clientDefault { Clock.System.now().toLocalDateTime(TimeZone.UTC) }
}

inline fun LongIdWithMetaTable.selectLatest(where: SqlExpressionBuilder.() -> Op<Boolean>) = select(where)
    .orderBy(CustomersDataTable.creationDate)
    .limit(1)
    .lastOrNull()