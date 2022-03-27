package beauty.shafran.network.employees.data

import com.arkivanov.essenty.parcelable.Parcelable
import com.arkivanov.essenty.parcelable.Parcelize
import kotlinx.serialization.Serializable
import beauty.shafran.network.assets.data.Asset
import beauty.shafran.network.assets.data.AssetType

@Parcelize
class AddEmployeeImageRequest(
    val data: AddEmployeeImageRequestData,
    val asset: Asset,
) : Parcelable


@Parcelize
@Serializable
data class AddEmployeeImageRequestData(
    val employeeId: String,
    val type: AssetType,
) : Parcelable


@Parcelize
@Serializable
data class AddEmployeeImageResponse(
    val employee: Employee,
) : Parcelable

