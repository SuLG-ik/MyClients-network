package beauty.shafran.network.session.entity

import beauty.shafran.network.utils.LongIdWithMetaTable
import kotlinx.serialization.Serializable
import ru.sulgik.exposed.TableToCreation

@TableToCreation
object ServiceSessionsStorageTable : LongIdWithMetaTable("service_session_storage")

@TableToCreation
object ServiceSessionsToCompanyStorageTable : LongIdWithMetaTable("service_session_storage_to_company") {
    val storageId = reference("storage", ServiceSessionsStorageTable)
    val companyId = reference("company", ServiceSessionsStorageTable)
}

@TableToCreation
object ServiceSessionsToStationStorageTable : LongIdWithMetaTable("service_session_storage_to_company_station") {
    val storageId = reference("storage", ServiceSessionsStorageTable)
    val stationId = reference("station", ServiceSessionsStorageTable)
}

@JvmInline
@Serializable
value class ServiceSessionStorageId(
    val id: Long
)