package beauty.shafran.network.employees.data

import beauty.shafran.network.assets.data.AssetData
import beauty.shafran.network.gender.Gender
import com.arkivanov.essenty.parcelable.Parcelable
import com.arkivanov.essenty.parcelable.Parcelize
import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable

@JvmInline
@Serializable
value class EmployeeId(
    val id: Long,
)

@Parcelize
@Serializable
data class Employee(
    val id: EmployeeId,
    val data: EmployeeData,
    val layoff: EmployeeLayoff?,
    val image: AssetData?,
) : Parcelable

@Parcelize
@Serializable
data class EmployeeData(
    val name: String,
    val description: String,
    val gender: Gender,
) : Parcelable

@Parcelize
@Serializable
data class EmployeeLayoff(
    val date: LocalDateTime,
    val note: String,
) : Parcelable

@JvmInline
@Serializable
value class EmployeeLayoffId(
    val id: Long,
)