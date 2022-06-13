package beauty.shafran.network.companies.data

import kotlinx.serialization.Serializable

@Serializable
data class CreatePlacementRequest(
    val companyId: CompanyId,
    val codename: CompanyPlacementCodename,
    val title: String? = null,
)


@Serializable
data class CreatePlacementResponse(
    val placement: CompanyPlacement,
)