package beauty.shafran.network.companies.repository

import beauty.shafran.network.accounts.data.AccountId
import beauty.shafran.network.companies.data.*
import beauty.shafran.network.companies.tables.*
import beauty.shafran.network.database.TransactionalScope
import org.jetbrains.exposed.sql.JoinType
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.select

class CompanyPlacementRepositoryImpl : CompanyPlacementRepository {

    context(TransactionalScope) private suspend fun getCompanyPlacementEntity(placementId: CompanyPlacementId): CompanyPlacementEntity {
        return CompanyPlacementTable.findById(placementId.value) ?: throw CompanyPlacementNotExists()
    }

    context(TransactionalScope) private suspend fun getCompanyPlacementsEntity(placementIds: List<CompanyPlacementId>): List<CompanyPlacementEntity> {
        val placements =
            CompanyPlacementTable.select { CompanyPlacementTable.id inList placementIds.map { it.value } }.map {
                it.toCompanyPlacementEntity()
            }
        if (placements.size != placementIds.size) {
            throw CompanyPlacementNotExists()
        }
        return placements
    }


    context(TransactionalScope) override suspend fun isPlacementInCompany(
        companyId: CompanyId,
        placementId: CompanyPlacementId,
    ): Boolean {
        return getCompanyPlacementEntity(placementId).companyId == companyId.value
    }

    context(TransactionalScope) override suspend fun isPlacementInCompanyStrict(
        companyId: CompanyId,
        placementId: CompanyPlacementId,
    ) {
        if (!isPlacementInCompany(companyId, placementId))
            throw PlacementNotInCompany()
    }

    context(TransactionalScope) override suspend fun isPlacementsInCompany(
        companyId: CompanyId,
        placementIds: List<CompanyPlacementId>,
    ): Boolean {
        return getCompanyPlacementsEntity(placementIds).all { it.companyId == companyId.value }
    }

    context(TransactionalScope) override suspend fun isPlacementsInCompanyStrict(
        companyId: CompanyId,
        placementIds: List<CompanyPlacementId>,
    ) {
        if (!isPlacementsInCompany(companyId, placementIds))
            throw PlacementNotInCompany()
    }

    context(TransactionalScope) override suspend fun createPlacement(
        companyId: CompanyId,
        codename: CompanyPlacementCodename,
        title: String?,
    ): Pair<CompanyPlacementEntity, CompanyPlacementDataEntity> {
        val placement = CompanyPlacementTable.insertAndGetEntity(codename = codename.value, companyId = companyId.value)
        val data =
            CompanyPlacementDataTable.insertAndGetEntity(placementId = placement.id, title = title ?: codename.value)
        return placement to data
    }

    context(TransactionalScope) override suspend fun getAvailablePlacements(
        companyId: CompanyId,
        accountId: AccountId,
    ): List<Pair<CompanyPlacementEntity, CompanyPlacementDataEntity>> {
        val member = CompanyMemberTable.select { CompanyMemberTable.accountId eq accountId.value }
            .firstOrNull()?.toCompanyMemberEntity() ?: throw AccountNotMemberOfCompany()
        return CompanyPlacementMemberTable.join(
            otherTable = CompanyPlacementTable,
            joinType = JoinType.RIGHT,
            additionalConstraint = { CompanyPlacementMemberTable.placementId eq CompanyPlacementTable.id }
        )
            .join(
                otherTable = CompanyPlacementDataTable,
                joinType = JoinType.RIGHT,
                additionalConstraint = { CompanyPlacementTable.id eq CompanyPlacementDataTable.placementId }
            )
            .select { CompanyPlacementMemberTable.memberId eq member.id }
            .map {
                it.toCompanyPlacementEntity() to it.toCompanyPlacementDataEntity()
            }
    }

    context(TransactionalScope) override suspend fun getCompanyPlacementsList(companyId: CompanyId): List<Pair<CompanyPlacementEntity, CompanyPlacementDataEntity>> {
        return CompanyPlacementTable
            .join(
                otherTable = CompanyPlacementDataTable,
                joinType = JoinType.RIGHT,
                additionalConstraint = { CompanyPlacementTable.id eq CompanyPlacementDataTable.placementId }
            )
            .select { CompanyPlacementTable.companyId eq companyId.value }
            .map { it.toCompanyPlacementEntity() to it.toCompanyPlacementDataEntity() }
    }

    context(TransactionalScope) override suspend fun isMemberOfPlacement(
        accountId: AccountId,
        placementId: CompanyPlacementId,
    ): Boolean {
        return CompanyPlacementMemberTable
            .select { (CompanyPlacementMemberTable.accountId eq accountId.value) and (CompanyPlacementMemberTable.placementId eq placementId.value) }
            .count() > 0
    }
}