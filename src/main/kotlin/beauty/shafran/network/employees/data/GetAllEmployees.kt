package beauty.shafran.network.employees.data

import com.arkivanov.essenty.parcelable.Parcelable
import com.arkivanov.essenty.parcelable.Parcelize
import kotlinx.serialization.Serializable

@Parcelize
@Serializable
data class GetAllEmployeesRequest(
    val page: Int? = null,
    val sort: String? = null,
) : Parcelable

@Parcelize
@Serializable
data class GetAllEmployeesResponse(
    val employees: List<Employee>,
) : Parcelable