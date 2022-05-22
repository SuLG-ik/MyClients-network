package beauty.shafran.network.companies.repository

import beauty.shafran.network.account.data.AccountId
import beauty.shafran.network.companies.data.CompanyId
import beauty.shafran.network.companies.entity.CompanyEntity
import beauty.shafran.network.companies.entity.CompanyEntityData
import beauty.shafran.network.companies.entity.CompanyMemberEntity
import beauty.shafran.network.utils.TransactionalScope

interface CompaniesRepository {

    suspend fun TransactionalScope.findCompanyById(companyId: CompanyId): CompanyEntity

    suspend fun TransactionalScope.findAccountMembersForAccount(accountId: AccountId): List<CompanyMemberEntity>

    suspend fun TransactionalScope.findCompanyForAccount(accountId: AccountId): List<CompanyEntity>

    suspend fun TransactionalScope.createCompany(data: CompanyEntityData, ownerAccountId: AccountId): CompanyEntity

    suspend fun TransactionalScope.createCompany(data: CompanyEntityData): CompanyEntity

    suspend fun TransactionalScope.pairCompanyToOwner(companyId: CompanyId, ownerAccountId: AccountId)

    suspend fun TransactionalScope.throwIfCompanyNotExists(companyId: CompanyId)

    suspend fun TransactionalScope.isCompanyExists(companyId: CompanyId): Boolean

}