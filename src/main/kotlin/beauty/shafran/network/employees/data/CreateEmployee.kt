package beauty.shafran.network.employees.data

import beauty.shafran.network.Gender
import com.arkivanov.essenty.parcelable.Parcelable
import com.arkivanov.essenty.parcelable.Parcelize
import kotlinx.serialization.Serializable
import javax.validation.Valid
import javax.validation.constraints.Size


@Parcelize
@Serializable
data class CreateEmployeeRequest(
    @field:Valid
    val data: CreateEmployeeRequestData,
) : Parcelable

@Parcelize
@Serializable
data class CreateEmployeeRequestData(
    @field:Size(min = 2, max = 64, message = "Name length must be between 2 and 64")
    val name: String,
    val gender: Gender,
    val description: String,
) : Parcelable


fun CreateEmployeeRequestData.trim(): CreateEmployeeRequestData {
    return copy(name = name.trim(), description = description.trim())
}

fun CreateEmployeeRequest.trim(): CreateEmployeeRequest {
    return copy(data = data.trim())
}

@Parcelize
@Serializable
data class CreateEmployeeResponse(
    val employee: Employee,
) : Parcelable