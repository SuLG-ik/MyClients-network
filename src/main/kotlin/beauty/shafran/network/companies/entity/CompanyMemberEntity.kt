package beauty.shafran.network.companies.entity

import beauty.shafran.network.utils.MetaEntity
import kotlinx.serialization.Contextual
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.litote.kmongo.Id
import org.litote.kmongo.newId

@Serializable
data class CompanyMemberEntity(
    val companyId: String,
    val accountId: String,
    val rawAccessedScopes: List<AccessScope>,
    val meta: MetaEntity = MetaEntity(),
    @Contextual
    @SerialName("_id")
    val id: Id<CompanyMemberEntity> = newId(),
) {

    val formattedAccessScope: List<String>
        get() = rawAccessedScopes.map { "{${it.code}}${companyId}" }

}