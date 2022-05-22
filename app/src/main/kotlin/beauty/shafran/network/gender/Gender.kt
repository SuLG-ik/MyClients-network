package beauty.shafran.network.gender

import beauty.shafran.network.utils.customEnumeration
import org.jetbrains.exposed.sql.Table

enum class Gender {
    MALE, FEMALE, UNKNOWN;

    companion object
}

val Gender.Companion.typeName get() = "gender"

fun Table.genderEnumeration(columnName: String) =
    customEnumeration<Gender>(columnName, Gender.typeName)