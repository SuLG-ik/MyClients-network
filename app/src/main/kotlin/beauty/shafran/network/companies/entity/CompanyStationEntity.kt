package beauty.shafran.network.companies.entity

import beauty.shafran.network.location.point
import beauty.shafran.network.utils.LongIdWithMetaTable
import kotlinx.serialization.Serializable
import ru.sulgik.exposed.TableToCreation


@TableToCreation
object CompanyStationTable : LongIdWithMetaTable("company_station") {
    val codename = varchar("codename", 64)
}

@TableToCreation
object CompanyStationAddressTable : LongIdWithMetaTable("company_station_address") {
    val countryCode = varchar("country_code", 8)
    val state = varchar("state", 255)
    val city = varchar("city", 255)
    val street = varchar("street", 255)
    val buildingNumber = varchar("building_number", 32)
    val office = varchar("office", 16)
    val zip = varchar("zip", 32)
    val stationId = reference("station", CompanyStationTable)
}

@TableToCreation
object CompanyStationLocationTable : LongIdWithMetaTable("company_station_location") {
    val location = point("location")
    val stationId = reference("station", CompanyStationTable)
}

@Serializable
data class CompanyStationEntityLocation(
    val location: LocationEntity,
    val address: AddressEntity,
)

@Serializable
data class LocationEntity(
    val latitude: String,
    val longitude: String,
)

@Serializable
data class AddressEntity(
    val countryCode: String,
    val state: String,
    val city: String,
    val streetName: String,
    val buildingNumber: String,
    val office: String,
    val zip: String,
)