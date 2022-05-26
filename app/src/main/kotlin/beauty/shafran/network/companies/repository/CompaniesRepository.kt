package beauty.shafran.network.companies.repository

import beauty.shafran.network.account.data.AccountId
import beauty.shafran.network.companies.data.CompanyId
import beauty.shafran.network.companies.data.CompanyMemberId
import beauty.shafran.network.companies.data.CreateCompanyRequestData
import beauty.shafran.network.companies.entity.CompanyDataEntity
import beauty.shafran.network.companies.entity.CompanyEntity
import beauty.shafran.network.companies.entity.CompanyMemberEntity
import beauty.shafran.network.companies.entity.CompanyOwnerEntity
import beauty.shafran.network.utils.PagedData
import beauty.shafran.network.utils.TransactionalScope

interface CompaniesRepository {

    context (TransactionalScope) suspend fun findCompanyById(companyId: CompanyId): CompanyEntity

    context (TransactionalScope) suspend fun findCompanyDataById(companyId: CompanyId): CompanyDataEntity

    context (TransactionalScope) suspend fun findCompaniesForAccount(
        accountId: AccountId,
        pagedData: PagedData,
    ): List<CompanyMemberEntity>

    context (TransactionalScope) suspend fun createCompany(
        request: CreateCompanyRequestData,
        ownerAccountId: AccountId,
    ): CompanyEntity

    context (TransactionalScope) suspend fun pairCompanyToOwner(
        companyId: CompanyId,
        memberId: CompanyMemberId,
    ): CompanyOwnerEntity

    context (TransactionalScope) suspend fun throwIfCompanyNotExists(companyId: CompanyId)

    context (TransactionalScope) suspend fun isCompanyExists(companyId: CompanyId): Boolean

    context(TransactionalScope) suspend fun findCompaniesById(companyIds: List<CompanyId>): List<CompanyEntity>

    context(TransactionalScope) suspend fun findCompaniesDataById(companyIds: List<CompanyId>): Map<CompanyId, CompanyDataEntity>

}