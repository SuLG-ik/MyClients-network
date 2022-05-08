package beauty.shafran.network.companies.entity

import beauty.shafran.network.utils.LongIdWithMetaTable
import beauty.shafran.network.utils.MetaEntity
import kotlinx.serialization.Serializable
import org.litote.kmongo.Id
import org.litote.kmongo.newId


object CompanyStationsTable : LongIdWithMetaTable("companies_stations") {

}


@Serializable
data class CompanyStationEntity(
    val companyId: String,
    val data: CompanyStationEntityData,
    val location: CompanyStationEntityLocation,
    val meta: MetaEntity = MetaEntity(),
    val id: Id<CompanyStationEntity> = newId(),
) {
    companion object {
        const val collectionName = "companies_stations"
    }
}

@Serializable
data class CompanyStationEntityData(
    val title: String,
    val description: String,
    val meta: MetaEntity = MetaEntity(),
)

@Serializable
data class CompanyStationEntityLocation(
    val location: LocationEntity,
    val address: AddressEntity,
    val meta: MetaEntity = MetaEntity(),
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