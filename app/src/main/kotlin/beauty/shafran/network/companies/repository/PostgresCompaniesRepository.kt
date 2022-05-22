package beauty.shafran.network.companies.repository

import beauty.shafran.CompanyNotExists
import beauty.shafran.network.account.data.AccountId
import beauty.shafran.network.companies.data.CompanyId
import beauty.shafran.network.companies.entity.*
import beauty.shafran.network.utils.MetaEntity
import beauty.shafran.network.utils.TransactionalScope
import beauty.shafran.network.utils.isRowExists
import beauty.shafran.network.utils.selectLatest
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.insertAndGetId
import org.koin.core.annotation.Single

@Single
class PostgresCompaniesRepository : CompaniesRepository {


    override suspend fun TransactionalScope.findCompanyById(companyId: CompanyId): CompanyEntity {
        val company =
            CompanyTable.selectLatest { CompanyTable.id eq companyId.id } ?: throw CompanyNotExists(companyId)
        val data = transactionAsync {
            CompanyDataTable.selectLatest { CompanyDataTable.companyId eq companyId.id }
                ?: throw CompanyNotExists(companyId)
        }
        return company.toCompanyEntity(data.await())
    }

    override suspend fun TransactionalScope.findAccountMembersForAccount(accountId: AccountId): List<CompanyMemberEntity> {
        TODO("Not yet implemented")
    }

    override suspend fun TransactionalScope.createCompany(data: CompanyEntityData): CompanyEntity {
        val time = kotlinx.datetime.Clock.System.now().toLocalDateTime(TimeZone.UTC)
        val companyId = CompanyTable.insertAndGetId {
            it[this.codename] = data.codename
            it[this.creationDate] = time
        }.value
        CompanyDataTable.insert {
            it[this.title] = data.title
            it[this.companyId] = companyId
        }
        return CompanyEntity(meta = MetaEntity(time), data = data, id = CompanyId(companyId))
    }

    override suspend fun TransactionalScope.pairCompanyToOwner(
        companyId: CompanyId,
        ownerAccountId: AccountId,
    ) {
        TODO("Not yet implemented")
    }

    override suspend fun TransactionalScope.findCompanyForAccount(accountId: AccountId): List<CompanyEntity> {
        TODO("Not yet implemented")
    }

    override suspend fun TransactionalScope.createCompany(
        data: CompanyEntityData,
        ownerAccountId: AccountId,
    ): CompanyEntity {
        val company = createCompany(data)
        pairCompanyToOwner(company.id, ownerAccountId)
        return company
    }

    override suspend fun TransactionalScope.throwIfCompanyNotExists(companyId: CompanyId) {
        if (!isCompanyExists(companyId)) throw CompanyNotExists(companyId)
    }


    override suspend fun TransactionalScope.isCompanyExists(companyId: CompanyId): Boolean {
        return CompanyTable.isRowExists { CompanyTable.id eq companyId.id }
    }
}