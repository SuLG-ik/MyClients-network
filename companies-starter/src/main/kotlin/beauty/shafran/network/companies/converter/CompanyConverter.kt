package beauty.shafran.network.companies.converter

import beauty.shafran.network.accounts.data.AccountId
import beauty.shafran.network.companies.data.Company
import beauty.shafran.network.companies.data.CompanyPlacement
import beauty.shafran.network.companies.tables.*

interface CompanyConverter {

    fun toCompanyPlacement(
        placementEntity: CompanyPlacementEntity,
        placementData: CompanyPlacementDataEntity,
        isMember: Boolean,
    ): CompanyPlacement

    fun toCompany(
        requestAccountId: AccountId,
        companyEntity: CompanyEntity,
        companyDataEntity: CompanyDataEntity,
        companyOwnerEntity: CompanyOwnerEntity,
        isMember: Boolean,
    ): Company

}