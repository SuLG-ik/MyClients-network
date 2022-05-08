package beauty.shafran.network.customers.entity

import kotlinx.serialization.Contextual
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.litote.kmongo.Id
import org.litote.kmongo.newId

@Serializable
class CustomersStorage(
    @SerialName("_id")
    @Contextual
    val id: Id<CustomersStorage> = newId(),
) {

    companion object {
        const val collectionName = "customers_storage"
    }

}


@Serializable
class CustomersStorageStationReference(
    val stationId: String,
    val storageId: String,
    @SerialName("_id")
    @Contextual
    val id: Id<CustomersStorageStationReference>,
) {

    companion object {
        const val collectionName = "customers_storage_station_reference"
    }

}


@Serializable
class CustomersStorageCompanyReference(
    val companyId: String,
    val storageId: String,
    @SerialName("_id")
    @Contextual
    val id: Id<CustomersStorageCompanyReference>,
) {

    companion object {
        const val collectionName = "customers_storage_company_reference"
    }

}