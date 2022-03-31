package beauty.shafran.network.employees.data

import com.arkivanov.essenty.parcelable.Parcelable
import com.arkivanov.essenty.parcelable.Parcelize
import jakarta.validation.constraints.Min
import kotlinx.serialization.Serializable

@Parcelize
@Serializable
data class GetAllEmployeesRequest(
    @field:Min(1)
    val offset: Int = 30,
    @field:Min(0)
    val page: Int = 0,
) : Parcelable {

}

@Parcelize
@Serializable
data class GetAllEmployeesResponse(
    val employees: List<Employee>,
) : Parcelable