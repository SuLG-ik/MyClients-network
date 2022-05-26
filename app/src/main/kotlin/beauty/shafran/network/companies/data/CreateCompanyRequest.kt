package beauty.shafran.network.companies.data

import beauty.shafran.network.account.data.AccountId
import com.arkivanov.essenty.parcelable.Parcelable
import com.arkivanov.essenty.parcelable.Parcelize
import kotlinx.serialization.Serializable

@Serializable
@Parcelize
data class CreateCompanyRequest(
    val data: CreateCompanyRequestData,
    val ownerAccountId: AccountId,
): Parcelable

@Serializable
@Parcelize
data class CreateCompanyRequestData(
    val companyTitle: String,
    val companyCodeName: String,
): Parcelable

@Serializable
@Parcelize
data class CreateCompanyResponse(
    val company: Company,
): Parcelable