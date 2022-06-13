package beauty.shafran.network.companies.repository

import beauty.shafran.network.accounts.data.AccountId
import beauty.shafran.network.companies.data.AccountNotMemberOfCompany
import beauty.shafran.network.companies.data.CompanyId
import beauty.shafran.network.companies.data.CompanyPlacementCodename
import beauty.shafran.network.companies.tables.*
import beauty.shafran.network.database.TransactionalScope
import org.jetbrains.exposed.sql.JoinType
import org.jetbrains.exposed.sql.select

class CompanyPlacementRepositoryImpl : CompanyPlacementRepository {

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

}