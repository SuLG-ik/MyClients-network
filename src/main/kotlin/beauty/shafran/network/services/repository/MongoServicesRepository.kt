package beauty.shafran.network.services.repository

import beauty.shafran.network.services.converter.ServicesConverter
import beauty.shafran.network.services.data.*
import beauty.shafran.network.services.enity.ServiceConfigurationEntity
import beauty.shafran.network.services.enity.ServiceEntity
import beauty.shafran.network.services.enity.ServiceInfoEntity
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList
import org.bson.types.ObjectId
import org.litote.kmongo.coroutine.CoroutineDatabase

class MongoServicesRepository(
    client: CoroutineDatabase,
    private val converter: ServicesConverter,
) : ServicesRepository {

    private val collection = client.getCollection<ServiceEntity>("services")

    override suspend fun getServices(data: GetAllServicesRequest): GetAllServicesResponse {
        val services = collection.find().toFlow().map { with(converter) { it.toData() } }
        return GetAllServicesResponse(services = services.toList())
    }

    override suspend fun createService(data: CreateServiceRequest): CreateServiceResponse {
        val service = ServiceEntity(
            info = ServiceInfoEntity(
                title = data.title,
                description = data.description
            )
        )
        collection.insertOne(service)
        return CreateServiceResponse(
            service = with(converter) { service.toData() }
        )
    }

    override suspend fun getServiceById(data: GetServiceByIdRequest): GetServiceByIdResponse {
        val service = collection.findOneById(ObjectId(data.serviceId))
        return GetServiceByIdResponse(
            service = with(converter) { service?.toData() }
        )
    }

    override suspend fun addConfiguration(data: CreateConfigurationRequest): CreateConfigurationResponse {
        val service = collection.findOneById(ObjectId(data.serviceId))!!
        val configurations = service.configurations.configurations
        val configuration = ServiceConfigurationEntity(
            title = data.data.title,
            description = data.data.description,
            cost = data.data.cost,
            amount = data.data.amount,
        )
        val newService =
            service.copy(configurations = service.configurations.copy(configurations = configurations + configuration))
        collection.save(newService)
        return CreateConfigurationResponse(
            service = with(converter) { newService.toData() }
        )
    }

    override suspend fun deactivateConfiguration(data: DeactivateServiceConfigurationRequest): DeactivateServiceConfigurationResponse {
        TODO("Not yet implemented")
    }
}