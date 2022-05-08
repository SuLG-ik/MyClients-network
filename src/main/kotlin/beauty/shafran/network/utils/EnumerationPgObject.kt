package beauty.shafran.network.utils

import beauty.shafran.network.customers.entity.CardsTable.customEnumeration
import org.postgresql.util.PGobject

@Suppress("FunctionName")
fun <T : Enum<T>> EnumPGObject(typeName: String, value: T): PGobject {
    return PGobject().apply {
        type = typeName
        setValue(value.name)
    }
}

inline fun <reified T : Enum<T>> customEnumeration(name: String, sql: String) =
    customEnumeration(
        name = name,
        sql = sql,
        fromDb = { enumValueOf<T>(it as String) },
        toDb = { EnumPGObject(sql, it) },
    )