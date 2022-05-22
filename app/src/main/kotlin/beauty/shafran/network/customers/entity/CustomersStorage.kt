package beauty.shafran.network.customers.entity

import beauty.shafran.network.companies.entity.CompanyStationTable
import beauty.shafran.network.companies.entity.CompanyTable
import beauty.shafran.network.utils.LongIdWithMetaTable
import ru.sulgik.exposed.PropertyOfEntity
import ru.sulgik.exposed.TableToCreation
import ru.sulgik.exposed.TableWithEntity

@TableToCreation
@TableWithEntity
object CustomerStorageTable : LongIdWithMetaTable("customer_storage")

@TableToCreation
@TableWithEntity
object CustomerStorageToCompanyTable : LongIdWithMetaTable("customer_storage_to_company") {
    @PropertyOfEntity
    val storageId = reference("storage", CustomerStorageTable)

    @PropertyOfEntity
    val companyId = reference("company", CompanyTable)
}

@TableToCreation
@TableWithEntity
object CustomerStorageToCompanyStationTable : LongIdWithMetaTable("customer_storage_to_company_station") {
    @PropertyOfEntity
    val storageId = reference("storage", CustomerStorageTable)

    @PropertyOfEntity
    val stationId = reference("station", CompanyStationTable)
}
