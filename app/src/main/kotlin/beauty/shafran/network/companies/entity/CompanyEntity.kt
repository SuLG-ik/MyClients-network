package beauty.shafran.network.companies.entity

import beauty.shafran.network.companies.data.CompanyId
import beauty.shafran.network.utils.LongIdWithMetaTable
import beauty.shafran.network.utils.MetaEntity
import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.ResultRow
import ru.sulgik.exposed.TableToCreation

@TableToCreation
object CompanyTable : LongIdWithMetaTable("company") {
    val codename = varchar("codename", 64).uniqueIndex()
}

@TableToCreation
object CompanyDataTable : LongIdWithMetaTable("company_data") {
    val title = varchar("title", 255)
    val companyId = reference("company", CompanyTable)
}

fun ResultRow.toCompanyEntity(companiesDataResult: ResultRow): CompanyEntity {
    return CompanyEntity(
        id = CompanyId(get(CompanyTable.id).value),
        data = CompanyEntityData(
            codename = get(CompanyTable.codename),
            title = companiesDataResult[CompanyDataTable.title]
        ),
        meta = MetaEntity(
            creationDate = get(CompanyTable.creationDate)
        )
    )
}

data class CompanyEntity(
    val data: CompanyEntityData,
    val meta: MetaEntity,
    val id: CompanyId,
)

@Serializable
data class CompanyEntityData(
    val title: String,
    val codename: String,
)