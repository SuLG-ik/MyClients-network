package beauty.shafran.network.gender

import beauty.shafran.network.utils.customEnumeration

enum class Gender {
    MALE, FEMALE, UNKNOWN;

    companion object
}

val Gender.Companion.typeName get() = "Gender"

fun genderEnumeration(columnName: String) =
    customEnumeration<Gender>(columnName, Gender.typeName)