package beauty.shafran.network.employees.data

import beauty.shafran.network.utils.PagedData
import com.arkivanov.essenty.parcelable.Parcelable
import com.arkivanov.essenty.parcelable.Parcelize
import kotlinx.serialization.Serializable

@Parcelize
@Serializable
data class GetAllEmployeesRequest(
    val storageId: EmployeeStorageId,
    val paged: PagedData,
) : Parcelable

@Parcelize
@Serializable
data class GetAllEmployeesResponse(
    val employees: List<Employee>,
    val paged: PagedData,
) : Parcelable