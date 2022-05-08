package beauty.shafran.network.services.executor

import beauty.shafran.network.auth.AuthorizationValidator
import beauty.shafran.network.auth.data.AuthorizedAccount
import beauty.shafran.network.auth.throwIfNotAccessedForCompany
import beauty.shafran.network.companies.data.CompanyId
import beauty.shafran.network.companies.entity.AccessScope
import beauty.shafran.network.services.converter.ServicesConverter
import beauty.shafran.network.services.data.*
import beauty.shafran.network.services.enity.ServiceConfigurationEntity
import beauty.shafran.network.services.enity.ServiceInfoEntity
import beauty.shafran.network.services.enity.toCompanyId
import beauty.shafran.network.services.repository.ServicesRepository
import org.koin.core.annotation.Single

@Single
class ServicesExecutorImpl(
    private val converter: ServicesConverter,
    private val servicesRepository: ServicesRepository,
    private val auth: AuthorizationValidator,
) : ServicesExecutor {


    override suspend fun getServices(
        request: GetAllServicesRequest,
        account: AuthorizedAccount,
    ): GetAllServicesResponse {
        auth.throwIfNotAccessedForCompany(companyId = request.companyId, scope = AccessScope.SERVICES_READ, account)
        val services =
            servicesRepository.findAllServices(request.offset, request.page, companyId = request.companyId.id)
        return GetAllServicesResponse(
            services = with(converter) { services.map { it.toData() } },
            offset = request.offset,
            page = request.page,
        )
    }

    override suspend fun editService(request: EditServiceRequest, account: AuthorizedAccount): EditServiceResponse {
        val service = servicesRepository.findServiceById(request.serviceId)
        auth.throwIfNotAccessedForCompany(service.companyReference.toCompanyId(),
            AccessScope.SERVICES_READ,
            account)
        val info = with(converter) { request.data.toNewEntity() }
        val newService = servicesRepository.updateServiceInfo(request.serviceId, info)
        return EditServiceResponse(
            service = with(converter) { newService.toData() }
        )
    }


    override suspend fun createService(
        request: CreateServiceRequest,
        account: AuthorizedAccount,
    ): CreateServiceResponse {
        auth.throwIfNotAccessedForCompany(CompanyId(request.companyId), AccessScope.SERVICES_ADD, account)
        val data = request.data
        val service = servicesRepository.createService(ServiceInfoEntity(
            title = data.title,
            description = data.description,
        ), companyId = request.companyId)
        return CreateServiceResponse(
            service = with(converter) { service.toData() }
        )
    }

    override suspend fun getServiceById(
        request: GetServiceByIdRequest,
        account: AuthorizedAccount,
    ): GetServiceByIdResponse {
        val service = servicesRepository.findServiceById(request.serviceId)
        auth.throwIfNotAccessedForCompany(service.companyReference.toCompanyId(), AccessScope.SERVICES_ADD, account)
        return GetServiceByIdResponse(
            service = with(converter) { service.toData() }
        )
    }

    override suspend fun addConfiguration(
        request: CreateConfigurationRequest,
        account: AuthorizedAccount,
    ): CreateConfigurationResponse {
        val service = servicesRepository.findServiceById(request.serviceId)
        auth.throwIfNotAccessedForCompany(service.companyReference.toCompanyId(), AccessScope.SERVICES_ADD, account)
        val data = request
        val newService = servicesRepository.addConfiguration(data.serviceId, ServiceConfigurationEntity(
            title = data.data.title,
            description = data.data.description,
            cost = data.data.cost,
            amount = data.data.amount,
        ))
        return CreateConfigurationResponse(
            service = with(converter) { newService.toData() }
        )
    }

    override suspend fun deactivateConfiguration(
        request: DeactivateServiceConfigurationRequest,
        account: AuthorizedAccount,
    ): DeactivateServiceConfigurationResponse {
        TODO("Not yet implemented")
    }
}