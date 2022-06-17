package beauty.shafran.network.services.tables

import beauty.shafran.network.companies.tables.CompanyTable
import beauty.shafran.network.exposed.LongIdWithMetaTable
import ru.sulgik.exposed.PropertyOfEntity
import ru.sulgik.exposed.TableToCreation
import ru.sulgik.exposed.TableWithEntity

@TableToCreation
@TableWithEntity
object ServiceTable : LongIdWithMetaTable("service") {

    @PropertyOfEntity
    val companyId = reference("company_id", CompanyTable)

}