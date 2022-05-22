package beauty.shafran.network.cards.entity

import beauty.shafran.network.companies.entity.CompanyStationTable
import beauty.shafran.network.companies.entity.CompanyTable
import beauty.shafran.network.utils.LongIdWithMetaTable
import ru.sulgik.exposed.TableToCreation

@TableToCreation
object CardStorageTable : LongIdWithMetaTable("card_storage")

@TableToCreation
object CardsStorageToCompanyTable : LongIdWithMetaTable("card_storage_to_company") {
    val storageId = reference("storage", CardStorageTable)
    val companyId = reference("company", CompanyTable)
}

@TableToCreation
object CardsStorageToCompanyStationTable : LongIdWithMetaTable("card_storage_to_company_station") {
    val storageId = reference("storage", CardStorageTable)
    val stationId = reference("station", CompanyStationTable)
}