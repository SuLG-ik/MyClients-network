package beauty.shafran.network.utils

import org.jetbrains.exposed.sql.Table
import org.postgresql.util.PGobject

@Suppress("FunctionName")
fun <T : Enum<T>> EnumPGObject(typeName: String, value: T): PGobject {
    return PGobject().apply {
        type = typeName
        setValue(value.name)
    }
}

inline fun <reified T : Enum<T>> Table.customEnumeration(name: String, sql: String) =
    customEnumeration(
        name = name,
        sql = sql,
        fromDb = { enumValueOf<T>(it as String) },
        toDb = { EnumPGObject(sql, it) },
    )