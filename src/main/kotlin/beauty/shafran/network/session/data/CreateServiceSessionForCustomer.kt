package beauty.shafran.network.session.data

import com.arkivanov.essenty.parcelable.Parcelable
import com.arkivanov.essenty.parcelable.Parcelize
import kotlinx.serialization.Serializable

@Parcelize
@Serializable
data class CreateSessionForCustomerRequest(
    val configuration: ServiceConfigurationReference,
    val customerId: String,
    val employeeId: String,
    val data: CreateServiceSessionForCustomerRequestData,
):Parcelable

@Serializable
@Parcelize
data class ServiceConfigurationReference(
    val serviceId: String,
    val configurationId: String,
): Parcelable

@Parcelize
@Serializable
data class CreateServiceSessionForCustomerRequestData(
    val remark: String? = null,
):Parcelable

@Parcelize
@Serializable
data class CreateSessionForCustomerResponse(
    val session: Session,
): Parcelable