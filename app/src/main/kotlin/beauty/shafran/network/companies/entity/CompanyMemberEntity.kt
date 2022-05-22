package beauty.shafran.network.companies.entity

import beauty.shafran.network.account.data.AccountId
import beauty.shafran.network.companies.data.CompanyId
import beauty.shafran.network.companies.data.CompanyMemberId
import beauty.shafran.network.companies.data.CompanyStationId
import beauty.shafran.network.companies.data.CompanyStationMemberId
import beauty.shafran.network.utils.MetaEntity


data class CompanyStationMemberEntity(
    val stationId: CompanyStationId,
    val accountId: AccountId,
    val meta: MetaEntity,
    val id: CompanyStationMemberId,
)

data class CompanyMemberEntity(
    val companyId: CompanyId,
    val accountId: AccountId,
    val meta: MetaEntity,
    val id: CompanyMemberId,
)

