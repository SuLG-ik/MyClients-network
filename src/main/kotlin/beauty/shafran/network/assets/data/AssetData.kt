@file:UseSerializers(ZonedDateTimeSerializer::class)
package beauty.shafran.network.assets.data

import com.arkivanov.essenty.parcelable.Parcelable
import com.arkivanov.essenty.parcelable.Parcelize
import kotlinx.serialization.Serializable
import kotlinx.serialization.UseSerializers
import beauty.shafran.network.ZonedDateTimeSerializer
import java.time.ZonedDateTime

@Parcelize
@Serializable
data class AssetData(
    val url: String,
    val hash: String,
    val type: AssetType,
    val date: ZonedDateTime,
):Parcelable