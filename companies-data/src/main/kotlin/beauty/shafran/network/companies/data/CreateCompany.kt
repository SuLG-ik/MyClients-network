package beauty.shafran.network.companies.data

import kotlinx.serialization.Serializable

@Serializable
data class CreateCompanyRequest(
    val codename: CompanyCodename,
    val title: String? = null,
)

@Serializable
data class CreateCompanyResponse(
    val company: Company,
)