package beauty.shafran.network.utils

import org.jetbrains.exposed.dao.id.IdTable
import org.jetbrains.exposed.sql.Op
import org.jetbrains.exposed.sql.SqlExpressionBuilder
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.select


inline fun Table.isRowExists(
    where: SqlExpressionBuilder.() -> Op<Boolean>,
): Boolean {
    return !select(where).empty()
}


fun <T : Comparable<T>> IdTable<T>.isRowExists(id: T): Boolean {
    return isRowExists { this@isRowExists.id eq id }
}

