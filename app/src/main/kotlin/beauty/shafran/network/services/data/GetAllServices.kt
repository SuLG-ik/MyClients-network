package beauty.shafran.network.services.data

import beauty.shafran.network.utils.PagedData
import com.arkivanov.essenty.parcelable.Parcelable
import com.arkivanov.essenty.parcelable.Parcelize
import kotlinx.serialization.Serializable

@Parcelize
@Serializable
data class GetAllServicesRequest(
    val paged: PagedData,
    val storageId: ServicesStorageId,
) : Parcelable

@Parcelize
@Serializable
data class GetAllServicesResponse(
    val services: List<Service>,
    val paged: PagedData,
) : Parcelable