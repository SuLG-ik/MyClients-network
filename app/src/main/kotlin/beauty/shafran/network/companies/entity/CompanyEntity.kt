package beauty.shafran.network.companies.entity

import beauty.shafran.network.account.entity.AccountTable
import beauty.shafran.network.utils.LongIdWithMetaTable
import ru.sulgik.exposed.PropertyOfEntity
import ru.sulgik.exposed.TableToCreation
import ru.sulgik.exposed.TableWithEntity

@TableToCreation
@TableWithEntity
object CompanyTable : LongIdWithMetaTable("company") {
    @PropertyOfEntity
    val codename = varchar("codename", 64).uniqueIndex()
}

@TableToCreation
@TableWithEntity
object CompanyDataTable : LongIdWithMetaTable("company_data") {

    @PropertyOfEntity
    val title = varchar("title", 255)

    @PropertyOfEntity
    val companyId = reference("company", CompanyTable)

}

@TableToCreation
@TableWithEntity
object CompanyMemberTable : LongIdWithMetaTable("company_member") {

    @PropertyOfEntity
    val accountId = reference("account", AccountTable)

    @PropertyOfEntity
    val companyId = reference("company", CompanyTable)

}

@TableToCreation
@TableWithEntity
object CompanyOwnerTable : LongIdWithMetaTable("company_owner") {

    @PropertyOfEntity
    val companyId = reference("company", CompanyTable)

    @PropertyOfEntity
    val memberId = reference("member", CompanyMemberTable)


}