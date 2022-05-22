package beauty.shafran.network.services.enity

import beauty.shafran.network.companies.data.CompanyId
import beauty.shafran.network.companies.data.CompanyStationId
import beauty.shafran.network.companies.entity.CompanyStationTable
import beauty.shafran.network.companies.entity.CompanyTable
import beauty.shafran.network.services.data.ServicesStorageId
import beauty.shafran.network.utils.LongIdWithMetaTable
import ru.sulgik.exposed.TableToCreation

@TableToCreation
object ServiceStorageTable : LongIdWithMetaTable("service_storage")

@TableToCreation
object ServiceStorageToCompanyTable : LongIdWithMetaTable("service_storage_to_company") {
    val storageId = reference("storage", ServiceStorageTable)
    val companyId = reference("company", CompanyTable)
}

@TableToCreation
object CardStorageToCompanyStationTable : LongIdWithMetaTable("service_storage_to_company_station") {
    val storageId = reference("storage", ServiceStorageTable)
    val stationId = reference("station", CompanyStationTable)
}

class ServicesStorageEntity(
    val id: ServicesStorageId,
    val companies: List<CompanyId>,
    val stations: List<CompanyStationId>,
)