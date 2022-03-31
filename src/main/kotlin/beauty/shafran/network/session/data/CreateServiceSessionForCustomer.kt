package beauty.shafran.network.session.data

import beauty.shafran.network.validation.ObjectIdParameter
import com.arkivanov.essenty.parcelable.Parcelable
import com.arkivanov.essenty.parcelable.Parcelize
import jakarta.validation.Valid
import kotlinx.serialization.Serializable

@Parcelize
@Serializable
data class CreateSessionForCustomerRequest(
    @field:Valid
    val configuration: ServiceConfigurationReference,
    @field:ObjectIdParameter
    val customerId: String,
    @field:ObjectIdParameter
    val employeeId: String,
    val data: CreateServiceSessionForCustomerRequestData,
):Parcelable


@Serializable
@Parcelize
data class ServiceConfigurationReference(
    @field:ObjectIdParameter
    val serviceId: String,
    @field:ObjectIdParameter
    val configurationId: String,
): Parcelable

@Parcelize
@Serializable
data class CreateServiceSessionForCustomerRequestData(
    val note: String? = null,
):Parcelable

@Parcelize
@Serializable
data class CreateSessionForCustomerResponse(
    val session: Session,
): Parcelable