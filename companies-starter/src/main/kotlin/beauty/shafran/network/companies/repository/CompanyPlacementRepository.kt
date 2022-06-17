package beauty.shafran.network.companies.repository

import beauty.shafran.network.accounts.data.AccountId
import beauty.shafran.network.companies.data.CompanyId
import beauty.shafran.network.companies.data.CompanyPlacementCodename
import beauty.shafran.network.companies.data.CompanyPlacementId
import beauty.shafran.network.companies.tables.CompanyPlacementDataEntity
import beauty.shafran.network.companies.tables.CompanyPlacementEntity
import beauty.shafran.network.database.TransactionalScope

interface CompanyPlacementRepository {

    context (TransactionalScope) suspend fun isPlacementInCompany(
        companyId: CompanyId,
        placementId: CompanyPlacementId,
    ): Boolean

    context (TransactionalScope) suspend fun isPlacementInCompanyStrict(
        companyId: CompanyId,
        placementId: CompanyPlacementId,
    )

    context (TransactionalScope) suspend fun isPlacementsInCompany(
        companyId: CompanyId,
        placementIds: List<CompanyPlacementId>,
    ): Boolean

    context (TransactionalScope) suspend fun isPlacementsInCompanyStrict(
        companyId: CompanyId,
        placementIds: List<CompanyPlacementId>,
    )

    context (TransactionalScope) suspend fun createPlacement(
        companyId: CompanyId,
        codename: CompanyPlacementCodename,
        title: String?,
    ): Pair<CompanyPlacementEntity, CompanyPlacementDataEntity>

    context (TransactionalScope) suspend fun getAvailablePlacements(
        companyId: CompanyId,
        accountId: AccountId,
    ): List<Pair<CompanyPlacementEntity, CompanyPlacementDataEntity>>

    context (TransactionalScope) suspend fun getCompanyPlacementsList(
        companyId: CompanyId,
    ): List<Pair<CompanyPlacementEntity, CompanyPlacementDataEntity>>

    context (TransactionalScope) suspend fun isMemberOfPlacement(
        accountId: AccountId,
        placementId: CompanyPlacementId,
    ): Boolean

}