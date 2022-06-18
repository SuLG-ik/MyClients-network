package beauty.shafran.network.companies.repository

import beauty.shafran.network.accounts.data.AccountId
import beauty.shafran.network.companies.data.CompanyCodename
import beauty.shafran.network.companies.tables.CompanyDataEntity
import beauty.shafran.network.companies.tables.CompanyEntity
import beauty.shafran.network.companies.tables.CompanyOwnerEntity
import beauty.shafran.network.database.TransactionalScope
import beauty.shafran.network.paged.data.PagedDataRequest


interface CompanyRepository {


    context(TransactionalScope) suspend fun createCompany(
        codename: CompanyCodename,
        ownerId: AccountId,
        title: String,
    ): Triple<CompanyEntity, CompanyDataEntity, CompanyOwnerEntity>

    context(TransactionalScope) suspend fun getAvailableCompanies(
        accountId: AccountId,
        pagedData: PagedDataRequest?,
    ): List<Triple<CompanyEntity, CompanyDataEntity, CompanyOwnerEntity>>
}