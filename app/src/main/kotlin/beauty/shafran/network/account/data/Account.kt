package beauty.shafran.network.account.data

import beauty.shafran.network.assets.data.Asset
import com.arkivanov.essenty.parcelable.Parcelable
import com.arkivanov.essenty.parcelable.Parcelize
import kotlinx.serialization.Serializable

@Serializable
@Parcelize
data class Account(
    val id: AccountId,
    val data: AccountData,
    val image: Asset? = null,
) : Parcelable

@Serializable
@JvmInline
value class AccountId(val id: Long)

@Serializable
@JvmInline
value class AccountUsername(val username: String)

@Parcelize
@Serializable
data class AccountData(
    val name: String,
    val username: AccountUsername,
) : Parcelable
