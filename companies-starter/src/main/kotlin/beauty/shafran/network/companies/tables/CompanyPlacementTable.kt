package beauty.shafran.network.companies.tables

import beauty.shafran.network.exposed.LongIdWithMetaTable
import org.jetbrains.exposed.sql.ReferenceOption
import ru.sulgik.exposed.PropertyOfEntity
import ru.sulgik.exposed.TableToCreation
import ru.sulgik.exposed.TableWithEntity


@TableToCreation
@TableWithEntity
object CompanyPlacementTable : LongIdWithMetaTable("company_placement") {

    @PropertyOfEntity
    val codename = varchar("codename", 64).uniqueIndex()

    @PropertyOfEntity
    val companyId = reference("company_id", CompanyTable)

}

@TableToCreation
@TableWithEntity
object CompanyPlacementDataTable : LongIdWithMetaTable("company_placement_data") {

    @PropertyOfEntity
    val placementId = reference("placement_id", CompanyPlacementTable, onDelete = ReferenceOption.CASCADE)

    @PropertyOfEntity
    val title = varchar("title", 128)

}

@TableToCreation
@TableWithEntity
object CompanyPlacementMemberTable : LongIdWithMetaTable("company_placement_member") {

    @PropertyOfEntity
    val placementId = reference("placement_id", CompanyPlacementTable, onDelete = ReferenceOption.CASCADE)

    @PropertyOfEntity
    val memberId = reference("member_id", CompanyMemberTable, onDelete = ReferenceOption.RESTRICT)


}