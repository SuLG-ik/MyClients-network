package beauty.shafran.network.companies.data

import beauty.shafran.network.utils.MetaData
import com.arkivanov.essenty.parcelable.Parcelable
import com.arkivanov.essenty.parcelable.Parcelize
import kotlinx.serialization.Serializable

@Serializable
@Parcelize
data class Company(
    val id: CompanyId,
    val codename: CompanyCodename,
    val meta: MetaData,
    val data: CompanyData,
) : Parcelable


@JvmInline
@Serializable
@Parcelize
value class CompanyCodename(val codeName: String) : Parcelable

@JvmInline
@Serializable
@Parcelize
value class CompanyId(val id: Long) : Parcelable

@JvmInline
@Serializable
@Parcelize
value class CompanyMemberId(val id: Long) : Parcelable

@Serializable
@Parcelize
data class CompanyData(
    val title: String,
) : Parcelable


@JvmInline
@Serializable
@Parcelize
value class CompanyStationId(val id: Long) : Parcelable

@JvmInline
@Serializable
@Parcelize
value class CompanyStationMemberId(val id: Long) : Parcelable

