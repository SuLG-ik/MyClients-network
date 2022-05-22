package beauty.shafran.network.services.enity

import beauty.shafran.network.assets.entity.AssetEntity
import beauty.shafran.network.services.data.ServiceConfigurationId
import beauty.shafran.network.services.data.ServiceId
import beauty.shafran.network.utils.LongIdWithMetaTable
import ru.sulgik.exposed.TableToCreation


@TableToCreation
object ServicesTable : LongIdWithMetaTable("service")

@TableToCreation
object ServicesToStorageTable : LongIdWithMetaTable("service_to_storage") {
    val storageId = reference("storage", ServiceStorageTable)
    val serviceId = reference("service", ServicesTable)
}

@TableToCreation
object ServicesDataTable : LongIdWithMetaTable("service_data") {
    val title = varchar("title", 50)
    val description = text("description")
    val serviceId = reference("service", ServicesTable)
}

@TableToCreation
object ServicesConfigurationsTable : LongIdWithMetaTable("service_configuration") {
    val serviceId = reference("service", ServicesTable)
}


@TableToCreation
object ServicesConfigurationsDataTable : LongIdWithMetaTable("service_configuration_data") {
    val title = varchar("title", 50)
    val description = text("description")
    val cost = integer("cost")
    val configurationId = reference("configuration", ServicesConfigurationsTable)
}

@TableToCreation
object ServicesConfigurationsWithLimitTable : LongIdWithMetaTable("service_configuration_with_limit") {
    val amount = integer("amount")
    val configurationId = reference("configuration", ServicesConfigurationsTable)
}

data class ServiceEntity(
    val info: ServiceInfoEntity,
    val image: AssetEntity? = null,
    val id: ServiceId,
    val configurations: List<ServiceConfigurationEntity> = emptyList(),
)

data class ServiceInfoEntity(
    val title: String,
    val description: String,
)

data class ServiceConfigurationEntity(
    val data: ServiceConfigurationEntityData,
    val serviceId: ServiceId,
    val id: ServiceConfigurationId,
)

data class ServiceConfigurationEntityData(
    val title: String,
    val description: String,
    val cost: Int,
    val parameters: TypedServiceConfigurationEntity,
)

sealed class TypedServiceConfigurationEntity {

    data class WithAmountLimit(
        val amount: Int,
    ) : TypedServiceConfigurationEntity()

}