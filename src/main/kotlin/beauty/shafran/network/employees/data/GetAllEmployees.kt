package beauty.shafran.network.employees.data

import beauty.shafran.network.companies.data.CompanyId
import com.arkivanov.essenty.parcelable.Parcelable
import com.arkivanov.essenty.parcelable.Parcelize
import kotlinx.serialization.Serializable

@Parcelize
@Serializable
data class GetAllEmployeesRequest(
    val companyId: CompanyId,
    val offset: Int = 30,
    val page: Int = 0,
) : Parcelable

@Parcelize
@Serializable
data class GetAllEmployeesResponse(
    val employees: List<Employee>,
    val offset: Int,
    val page: Int,
) : Parcelable