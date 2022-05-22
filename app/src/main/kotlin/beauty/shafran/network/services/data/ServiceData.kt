package beauty.shafran.network.services.data

import beauty.shafran.network.assets.data.AssetData
import com.arkivanov.essenty.parcelable.Parcelable
import com.arkivanov.essenty.parcelable.Parcelize
import kotlinx.serialization.Serializable

@Serializable
@JvmInline
value class ServiceId(
    val id: Long,
)


@Serializable
@Parcelize
data class Service(
    val id: ServiceId,
    val data: ServiceData,
) : Parcelable


@Parcelize
@Serializable
data class ServiceData(
    val info: ServiceInfo,
    val image: AssetData? = null,
    val configurations: List<ServiceConfiguration> = emptyList(),
) : Parcelable

@Serializable
@Parcelize
data class ConfiguredService(
    val serviceId: ServiceId,
    val info: ServiceInfo,
    val image: AssetData? = null,
    val configuration: ServiceConfiguration,
) : Parcelable


@Serializable
@JvmInline
value class ServiceConfigurationId(
    val id: Long,
)

@Parcelize
@Serializable
data class ServiceInfo(
    val title: String,
    val description: String,
) : Parcelable


@Parcelize
@Serializable
data class ServiceConfiguration(
    val title: String,
    val description: String,
    val cost: Int,
    val amount: Int,
    val serviceId: ServiceId,
    val id: ServiceConfigurationId,
) : Parcelable
