package beauty.shafran.network.services.executor

import beauty.shafran.network.services.converter.ServicesConverter
import beauty.shafran.network.services.data.*
import beauty.shafran.network.services.enity.ServiceConfigurationEntity
import beauty.shafran.network.services.enity.ServiceInfoEntity
import beauty.shafran.network.services.repository.ServicesRepository
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.litote.kmongo.coroutine.CoroutineDatabase

class ServicesExecutorImpl : ServicesExecutor, KoinComponent {


    private val converter: ServicesConverter by inject()
    private val servicesRepository: ServicesRepository by inject()


    override suspend fun getServices(data: GetAllServicesRequest): GetAllServicesResponse {
        val services = servicesRepository.findAllServices(data.offset, data.page)
        return GetAllServicesResponse(services = with(converter) { services.map { it.toData() } })
    }

    override suspend fun createService(data: CreateServiceRequest): CreateServiceResponse {
        val service = servicesRepository.createService(ServiceInfoEntity(
            title = data.title,
            description = data.description,
        ))
        return CreateServiceResponse(
            service = with(converter) { service.toData() }
        )
    }

    override suspend fun getServiceById(data: GetServiceByIdRequest): GetServiceByIdResponse {
        val service = servicesRepository.findServiceById(data.serviceId)
        return GetServiceByIdResponse(
            service = with(converter) { service.toData() }
        )
    }

    override suspend fun addConfiguration(data: CreateConfigurationRequest): CreateConfigurationResponse {
        val service = servicesRepository.addConfiguration(data.serviceId, ServiceConfigurationEntity(
            title = data.data.title,
            description = data.data.description,
            cost = data.data.cost,
            amount = data.data.amount,
        ))
        return CreateConfigurationResponse(
            service = with(converter) { service.toData() }
        )
    }

    override suspend fun deactivateConfiguration(data: DeactivateServiceConfigurationRequest): DeactivateServiceConfigurationResponse {
        TODO("Not yet implemented")
    }
}