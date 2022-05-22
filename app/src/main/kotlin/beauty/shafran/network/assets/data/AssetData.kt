package beauty.shafran.network.assets.data

import com.arkivanov.essenty.parcelable.Parcelable
import com.arkivanov.essenty.parcelable.Parcelize
import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable

@Parcelize
@Serializable
data class AssetData(
    val url: String,
    val hash: String,
    val type: AssetType,
    val date: LocalDateTime,
) : Parcelable