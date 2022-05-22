package beauty.shafran.network.account.data

import com.arkivanov.essenty.parcelable.Parcelable
import com.arkivanov.essenty.parcelable.Parcelize
import kotlinx.serialization.Serializable

@Serializable
@Parcelize
class GetAttachedAccountRequest : Parcelable

@Serializable
@Parcelize
class GetAttachedAccountResponse(
    val account: Account,
) : Parcelable