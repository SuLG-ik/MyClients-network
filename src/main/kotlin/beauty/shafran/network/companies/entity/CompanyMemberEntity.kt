package beauty.shafran.network.companies.entity

import beauty.shafran.network.utils.MetaEntity
import kotlinx.serialization.Contextual
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.litote.kmongo.Id
import org.litote.kmongo.newId

@Serializable
data class CompanyStationMemberEntity(
    val stationId: String,
    val accountId: String,
    val scopes: List<Scope>,
    val meta: MetaEntity = MetaEntity(),
    @Contextual
    @SerialName("_id")
    val id: Id<CompanyStationMemberEntity> = newId(),
) {

    companion object {
        const val collectionName = "stations_members"
    }

}

@Serializable
data class CompanyMemberEntity(
    val companyId: String,
    val accountId: String,
    val scopes: List<Scope>,
    val meta: MetaEntity = MetaEntity(),
    @Contextual
    @SerialName("_id")
    val id: Id<CompanyMemberEntity> = newId(),
) {

    val formattedAccessScopes: List<String>
        get() = scopes.mapNotNull { if (it.isAccessed) "{${it.scope.code}}${companyId}" else null }

    companion object {
        const val collectionName = "companies_member"
    }

}

@Serializable
data class Scope(
    val scope: AccessScope,
    val isAccessed: Boolean,
)