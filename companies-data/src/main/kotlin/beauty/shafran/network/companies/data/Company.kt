package beauty.shafran.network.companies.data

import kotlinx.serialization.Serializable

@JvmInline
@Serializable
value class CompanyId(val value: Long)

@JvmInline
@Serializable
value class CompanyCodename(val value: String)

@Serializable
class Company(
    val id: CompanyId,
    val codename: CompanyCodename,
    val data: CompanyData,
    val isOwned: Boolean,
    val isMember: Boolean,
)

@Serializable
class CompanyData(
    val title: String,
)