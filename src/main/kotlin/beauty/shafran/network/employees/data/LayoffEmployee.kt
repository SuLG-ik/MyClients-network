package beauty.shafran.network.employees.data

import beauty.shafran.network.validation.ObjectIdParameter
import com.arkivanov.essenty.parcelable.Parcelable
import com.arkivanov.essenty.parcelable.Parcelize
import kotlinx.serialization.Serializable

@Parcelize
@Serializable
data class LayoffEmployeeRequest(
    @field:ObjectIdParameter("Illegal employee id")
    val employeeId: String,
    val data: LayoffEmployeeRequestData,
) : Parcelable

fun LayoffEmployeeRequest.trim(): LayoffEmployeeRequest {
    return copy(data = data.trim())
}

@Parcelize
@Serializable
data class LayoffEmployeeRequestData(
    val reason: String,
) : Parcelable

fun LayoffEmployeeRequestData.trim(): LayoffEmployeeRequestData {
    return copy(reason = reason.trim())
}

@Parcelize
@Serializable
data class LayoffEmployeeResponse(
    val employee: Employee,
) : Parcelable