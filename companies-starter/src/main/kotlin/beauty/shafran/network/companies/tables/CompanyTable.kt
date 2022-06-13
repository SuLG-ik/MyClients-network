package beauty.shafran.network.companies.tables

import beauty.shafran.network.accounts.tables.AccountTable
import beauty.shafran.network.exposed.LongIdWithMetaTable
import org.jetbrains.exposed.sql.ReferenceOption
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
    val companyId = reference("company_id", CompanyTable, onDelete = ReferenceOption.CASCADE)

    @PropertyOfEntity
    val title = varchar("title", 128)

}

@TableToCreation
@TableWithEntity
object CompanyMemberTable : LongIdWithMetaTable("company_member") {

    @PropertyOfEntity
    val companyId = reference("company_id", CompanyTable, onDelete = ReferenceOption.CASCADE)

    @PropertyOfEntity
    val accountId = reference("account_id", AccountTable, onDelete = ReferenceOption.RESTRICT)

}


@TableToCreation
@TableWithEntity
object CompanyOwnerTable : LongIdWithMetaTable("company_owner") {

    @PropertyOfEntity
    val companyId = reference("company_id", CompanyTable, onDelete = ReferenceOption.CASCADE)

    @PropertyOfEntity
    val accountId = reference("account_id", AccountTable, onDelete = ReferenceOption.RESTRICT)

    @PropertyOfEntity
    val memberId = reference("member_id", CompanyMemberTable, onDelete = ReferenceOption.RESTRICT, onUpdate = ReferenceOption.RESTRICT)

}