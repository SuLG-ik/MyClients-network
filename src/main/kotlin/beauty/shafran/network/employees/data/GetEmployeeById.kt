package beauty.shafran.network.employees.data

import com.arkivanov.essenty.parcelable.Parcelable
import com.arkivanov.essenty.parcelable.Parcelize
import kotlinx.serialization.Serializable

@Parcelize
@Serializable
data class GetEmployeeWithIdRequest(
    val employeeId: String,
) : Parcelable

@Parcelize
@Serializable
data class GetEmployeeByIdResponse(
    val employee: Employee,
) : Parcelable