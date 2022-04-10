package beauty.shafran.network.services.executor

import beauty.shafran.network.services.converter.ServicesConverter
import beauty.shafran.network.services.data.*
import beauty.shafran.network.services.enity.ServiceConfigurationEntity
import beauty.shafran.network.services.enity.ServiceInfoEntity
import beauty.shafran.network.services.repository.ServicesRepository
import org.springframework.stereotype.Service

@Service
class ServicesExecutorImpl(
    private val converter: ServicesConverter,
    private val servicesRepository: ServicesRepository,
) : ServicesExecutor {


    override suspend fun getServices(request: GetAllServicesRequest): GetAllServicesResponse {
        val services = servicesRepository.findAllServices(request.offset, request.page)
        return GetAllServicesResponse(
            services = with(converter) { services.map { it.toData() } },
            offset = request.offset,
            page = request.page,
        )
    }

    override suspend fun editService(request: EditServiceRequest): EditServiceResponse {
        val info = with(converter) { request.data.trim().toNewEntity() }
        val service = servicesRepository.updateServiceInfo(request.serviceId, info)
        return EditServiceResponse(
            service = with(converter) { service.toData() }
        )
    }


    override suspend fun createService(request: CreateServiceRequest): CreateServiceResponse {
        val data = request.data.trim()
        val service = servicesRepository.createService(ServiceInfoEntity(
            title = data.title,
            description = data.description,
        ))
        return CreateServiceResponse(
            service = with(converter) { service.toData() }
        )
    }

    override suspend fun getServiceById(request: GetServiceByIdRequest): GetServiceByIdResponse {
        val service = servicesRepository.findServiceById(request.serviceId)
        return GetServiceByIdResponse(
            service = with(converter) { service.toData() }
        )
    }

    override suspend fun addConfiguration(request: CreateConfigurationRequest): CreateConfigurationResponse {
        val data = request.trim()
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

    override suspend fun deactivateConfiguration(request: DeactivateServiceConfigurationRequest): DeactivateServiceConfigurationResponse {
        TODO("Not yet implemented")
    }
}