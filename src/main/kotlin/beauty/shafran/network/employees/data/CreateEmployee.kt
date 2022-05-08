package beauty.shafran.network.employees.data

import beauty.shafran.network.gender.Gender
import com.arkivanov.essenty.parcelable.Parcelable
import com.arkivanov.essenty.parcelable.Parcelize
import kotlinx.serialization.Serializable


@Parcelize
@Serializable
data class CreateEmployeeRequest(
    val data: CreateEmployeeRequestData,
    val companyId: String
) : Parcelable

@Parcelize
@Serializable
data class CreateEmployeeRequestData(
    val name: String,
    val gender: Gender,
    val description: String,
): Parcelable

@Parcelize
@Serializable
data class CreateEmployeeResponse(
    val employee: Employee,
) : Parcelable