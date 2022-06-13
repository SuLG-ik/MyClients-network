package beauty.shafran.network.companies.converter

import beauty.shafran.network.accounts.data.AccountId
import beauty.shafran.network.companies.data.*
import beauty.shafran.network.companies.tables.*

class CompanyConverterImpl : CompanyConverter {

    override fun toCompany(
        requestAccountId: AccountId,
        companyEntity: CompanyEntity,
        companyDataEntity: CompanyDataEntity,
        companyOwnerEntity: CompanyOwnerEntity,
        isMember: Boolean,
    ): Company {
        return Company(
            id = CompanyId(companyEntity.id),
            codename = CompanyCodename(companyEntity.codename),
            data = CompanyData(
                title = companyDataEntity.title,
            ),
            isOwned = companyOwnerEntity.accountId == requestAccountId.value,
            isMember = isMember,
        )
    }

    override fun toCompanyPlacement(
        placementEntity: CompanyPlacementEntity,
        placementData: CompanyPlacementDataEntity,
        isMember: Boolean,
    ): CompanyPlacement {
        return CompanyPlacement(
            id = CompanyPlacementId(placementEntity.id),
            companyId = CompanyId(placementEntity.companyId),
            codename = CompanyPlacementCodename(placementEntity.codename),
            data = CompanyPlacementData(placementData.title),
            isMember = isMember,
        )
    }

}