package beauty.shafran.network.session.data

import beauty.shafran.network.companies.data.CompanyStationId
import beauty.shafran.network.customers.data.CustomerId
import beauty.shafran.network.employees.data.EmployeeId
import beauty.shafran.network.services.data.ServiceConfigurationId
import beauty.shafran.network.session.entity.ServiceSessionStorageId
import com.arkivanov.essenty.parcelable.Parcelable
import com.arkivanov.essenty.parcelable.Parcelize
import kotlinx.serialization.Serializable

@Parcelize
@Serializable
data class CreateSessionForCustomerRequest(
    val configurationId: ServiceConfigurationId,
    val customerId: CustomerId,
    val employeeId: EmployeeId,
    val storageId: ServiceSessionStorageId,
    val stationId: CompanyStationId,
    val data: CreateServiceSessionForCustomerRequestData,
) : Parcelable


@Parcelize
@Serializable
data class CreateServiceSessionForCustomerRequestData(
    val note: String? = null,
) : Parcelable

@Parcelize
@Serializable
data class CreateSessionForCustomerResponse(
    val serviceSession: ServiceSession,
) : Parcelable