package beauty.shafran.network.companies.entity

import beauty.shafran.network.utils.LongIdWithMetaTable
import beauty.shafran.network.utils.MetaEntity
import kotlinx.serialization.Contextual
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.litote.kmongo.Id
import org.litote.kmongo.newId


object CompaniesTable : LongIdWithMetaTable("companies") {
    val codename = varchar("codename", 64)
}

object CompaniesDataTable : LongIdWithMetaTable("companies_data") {
    val title = varchar("title", 255)
    val companyId = reference("company", CompaniesTable)
}


@Serializable
data class CompanyEntity(
    val data: CompanyEntityData,
    val meta: MetaEntity = MetaEntity(),
    @SerialName("_id")
    @Contextual
    val id: Id<CompanyEntity> = newId(),
) {
    companion object {
        const val collectionName = "companies"
    }
}

@Serializable
data class CompanyEntityData(
    val title: String,
    val codeName: String,
)