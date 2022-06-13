package beauty.shafran.network.companies.data

import kotlinx.serialization.Serializable

@JvmInline
@Serializable
value class CompanyPlacementId(val value: Long)

@JvmInline
@Serializable
value class CompanyPlacementCodename(val value: String)

@Serializable
data class CompanyPlacementData(
    val title: String,
)

@Serializable
class CompanyPlacement(
    val id: CompanyPlacementId,
    val companyId: CompanyId,
    val codename: CompanyPlacementCodename,
    val data: CompanyPlacementData,
    val isMember: Boolean,
)