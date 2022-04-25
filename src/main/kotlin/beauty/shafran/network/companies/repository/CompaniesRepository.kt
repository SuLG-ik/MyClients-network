package beauty.shafran.network.companies.repository

import beauty.shafran.network.companies.entity.AccessScope
import beauty.shafran.network.companies.entity.CompanyEntity
import beauty.shafran.network.companies.entity.CompanyEntityData
import beauty.shafran.network.companies.entity.CompanyMemberEntity

interface CompaniesRepository {

    suspend fun findCompanyById(companyId: String): CompanyEntity

    suspend fun findAccountMembersOfAccount(accountId: String): List<CompanyMemberEntity>

    suspend fun isAccessedForCompany(
        accessScope: AccessScope,
        accountId: String,
        businessId: String,
    ): Boolean

    suspend fun findCompanyForAccount(accountId: String): List<CompanyEntity>

    suspend fun createBusiness(data: CompanyEntityData): CompanyEntity

    suspend fun throwIfBusinessNotExists(businessId: String)
}