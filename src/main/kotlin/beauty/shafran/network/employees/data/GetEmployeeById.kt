package beauty.shafran.network.employees.data

import beauty.shafran.network.validation.ObjectIdParameter
import com.arkivanov.essenty.parcelable.Parcelable
import com.arkivanov.essenty.parcelable.Parcelize
import kotlinx.serialization.Serializable

@Parcelize
@Serializable
data class GetEmployeeByIdRequest(
    @ObjectIdParameter
    val employeeId: String,
) : Parcelable

@Parcelize
@Serializable
data class GetEmployeeByIdResponse(
    val employee: Employee,
) : Parcelable