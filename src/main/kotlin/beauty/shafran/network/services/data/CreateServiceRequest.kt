package beauty.shafran.network.services.data

import com.arkivanov.essenty.parcelable.Parcelable
import com.arkivanov.essenty.parcelable.Parcelize
import kotlinx.serialization.Serializable


@Parcelize
@Serializable
data class CreateServiceRequest (
    val description: String,
    val title: String,
) : Parcelable



@Parcelize
@Serializable
data class CreateServiceResponse (
    val service: Service,
) : Parcelable

