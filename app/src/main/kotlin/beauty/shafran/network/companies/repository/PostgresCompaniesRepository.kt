package beauty.shafran.network.companies.repository

import beauty.shafran.CompanyNotExists
import beauty.shafran.network.account.data.AccountId
import beauty.shafran.network.companies.data.CompanyId
import beauty.shafran.network.companies.data.CompanyMemberId
import beauty.shafran.network.companies.data.CreateCompanyRequestData
import beauty.shafran.network.companies.entity.*
import beauty.shafran.network.utils.*
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.jetbrains.exposed.sql.select


class PostgresCompaniesRepository(
    private val clock: Clock,
) : CompaniesRepository {


    context (TransactionalScope) override suspend fun findCompanyById(companyId: CompanyId): CompanyEntity {
        return CompanyTable.findById(companyId.id) ?: throw CompanyNotExists(companyId)
    }

    context (TransactionalScope) private suspend fun createCompany(
        request: CreateCompanyRequestData,
    ): CompanyEntity {
        val currentTime = clock.now().toLocalDateTime(TimeZone.UTC)
        val company = CompanyTable.insertAndGetEntity(
            codename = request.companyCodeName,
            creationDate = currentTime
        )
        CompanyDataTable.insertEntity(
            title = request.companyTitle,
            companyId = company.id,
            creationDate = currentTime
        )
        return company
    }

    context(TransactionalScope) override suspend fun findCompanyDataById(companyId: CompanyId): CompanyDataEntity {
        return CompanyDataTable.selectLatest { CompanyDataTable.companyId eq companyId.id }?.toCompanyDataEntity()
            ?: throw CompanyNotExists(companyId)
    }

    context (TransactionalScope) override suspend fun pairCompanyToOwner(
        companyId: CompanyId,
        memberId: CompanyMemberId,
    ): CompanyOwnerEntity {
        return CompanyOwnerTable.insertAndGetEntity(companyId = companyId.id, memberId = memberId.id)
    }


    context (TransactionalScope) override suspend fun findCompaniesById(companyIds: List<CompanyId>): List<CompanyEntity> {
        return CompanyTable.select { CompanyTable.id inList companyIds.map { it.id } }
            .map { it.toCompanyEntity() }
    }

    context(TransactionalScope) override suspend fun findCompaniesDataById(companyIds: List<CompanyId>): Map<CompanyId, CompanyDataEntity> {
        return CompanyDataTable.select { CompanyDataTable.companyId inList companyIds.map { it.id } }
            .associate {
                val entity = it.toCompanyDataEntity()
                CompanyId(entity.companyId) to entity
            }
    }

    context (TransactionalScope) override suspend fun findCompaniesForAccount(
        accountId: AccountId,
        pagedData: PagedData,
    ): List<CompanyMemberEntity> {
        return CompanyMemberTable.select { CompanyMemberTable.accountId eq accountId.id }
            .paged(pagedData)
            .map { it.toCompanyMemberEntity() }
    }

    context (TransactionalScope) private suspend fun createCompanyMember(
        accountId: AccountId,
        companyId: CompanyId,
    ): CompanyMemberEntity {
        return CompanyMemberTable.insertAndGetEntity(
            companyId = companyId.id,
            accountId = accountId.id
        )
    }

    context (TransactionalScope) override suspend fun createCompany(
        request: CreateCompanyRequestData,
        ownerAccountId: AccountId,
    ): CompanyEntity {
        val company = createCompany(request)
        val companyMember = createCompanyMember(accountId = ownerAccountId, companyId = CompanyId(company.id))
        val companyOwnerEntity = pairCompanyToOwner(CompanyId(company.id), CompanyMemberId(companyMember.id))
        return company
    }

    context (TransactionalScope) override suspend fun throwIfCompanyNotExists(companyId: CompanyId) {
        if (!isCompanyExists(companyId)) throw CompanyNotExists(companyId)
    }


    context (TransactionalScope) override suspend fun isCompanyExists(companyId: CompanyId): Boolean {
        return CompanyTable.isRowExists { CompanyTable.id eq companyId.id }
    }
}