package beauty.shafran.network.employees.data

import com.arkivanov.essenty.parcelable.Parcelable
import com.arkivanov.essenty.parcelable.Parcelize
import kotlinx.serialization.Serializable

@Parcelize
@Serializable
data class LayoffEmployeeRequest(
    val employeeId: String,
    val data: LayoffEmployeeRequestData,
) : Parcelable

@Parcelize
@Serializable
data class LayoffEmployeeRequestData(
    val reason: String,
) : Parcelable

@Parcelize
@Serializable
data class LayoffEmployeeResponse(
    val employee: Employee,
) : Parcelable