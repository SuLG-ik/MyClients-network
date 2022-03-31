package beauty.shafran.network.employees.data

import beauty.shafran.network.Gender
import com.arkivanov.essenty.parcelable.Parcelable
import com.arkivanov.essenty.parcelable.Parcelize
import jakarta.validation.constraints.Size
import kotlinx.serialization.Serializable

@Parcelize
@Serializable
data class CreateEmployeeRequest(
    @field:Size(min = 2, max = 64, message = "Name length must be betwern 2 and 64")
    val name: String,
    val gender: Gender,
    val description: String,
) : Parcelable

fun CreateEmployeeRequest.trim(): CreateEmployeeRequest {
    return copy(name = name.trim(), description = description.trim())
}

@Parcelize
@Serializable
data class CreateEmployeeResponse(
    val employee: Employee,
) : Parcelable