package beauty.shafran.network.companies.repository

import beauty.shafran.network.accounts.data.AccountId
import beauty.shafran.network.companies.data.CompanyCodename
import beauty.shafran.network.companies.tables.*
import beauty.shafran.network.database.TransactionalScope
import beauty.shafran.network.paged.data.PagedData
import beauty.shafran.network.paged.exposed.paged
import org.jetbrains.exposed.sql.JoinType
import org.jetbrains.exposed.sql.select

internal class CompanyRepositoryImpl : CompanyRepository {


    context(TransactionalScope) override suspend fun createCompany(
        codename: CompanyCodename,
        ownerId: AccountId,
        title: String,
    ): Triple<CompanyEntity, CompanyDataEntity, CompanyOwnerEntity> {
        val company = CompanyTable.insertAndGetEntity(codename = codename.value)
        val data = CompanyDataTable.insertAndGetEntity(
            title = title,
            companyId = company.id
        )
        val member = CompanyMemberTable.insertAndGetEntity(
            companyId = company.id,
            accountId = ownerId.value
        )
        val owner = CompanyOwnerTable.insertAndGetEntity(
            companyId = company.id,
            memberId = member.id,
            accountId = member.accountId
        )
        return Triple(company, data, owner)
    }

    context(TransactionalScope) override suspend fun getAvailableCompanies(
        accountId: AccountId,
        pagedData: PagedData?,
    ): List<Triple<CompanyEntity, CompanyDataEntity, CompanyOwnerEntity>> {
        return CompanyMemberTable.join(
            otherTable = CompanyTable,
            joinType = JoinType.RIGHT,
            additionalConstraint = { CompanyMemberTable.companyId eq CompanyTable.id },
        )
            .join(
                CompanyDataTable,
                JoinType.RIGHT,
                additionalConstraint = { CompanyTable.id eq CompanyDataTable.companyId },
            )
            .join(
                CompanyOwnerTable,
                JoinType.RIGHT,
                additionalConstraint = { CompanyTable.id eq CompanyOwnerTable.companyId },
            )
            .select { CompanyMemberTable.accountId eq accountId.value }
            .paged(pagedData)
            .map {
                Triple(it.toCompanyEntity(), it.toCompanyDataEntity(), it.toCompanyOwnerEntity())
            }
    }

}