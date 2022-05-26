package beauty.shafran.network.services.executor

import beauty.shafran.network.auth.AuthorizationValidator
import beauty.shafran.network.auth.data.AuthorizedAccount
import beauty.shafran.network.services.converter.ServicesConverter
import beauty.shafran.network.services.data.*
import beauty.shafran.network.services.enity.ServiceConfigurationEntityData
import beauty.shafran.network.services.enity.ServiceInfoEntity
import beauty.shafran.network.services.enity.TypedServiceConfigurationEntity
import beauty.shafran.network.services.repository.ServicesRepository
import beauty.shafran.network.utils.Transactional


class ServicesExecutorImpl(
    private val converter: ServicesConverter,
    private val servicesRepository: ServicesRepository,
    private val auth: AuthorizationValidator,
    private val transactional: Transactional,
) : ServicesExecutor {


    override suspend fun getServices(
        request: GetAllServicesRequest,
        account: AuthorizedAccount,
    ): GetAllServicesResponse {
        return transactional.withSuspendedTransaction {
            val services =
                with(servicesRepository) { findAllServices(paged = request.paged, storageId = request.storageId) }
            GetAllServicesResponse(
                services = with(converter) { services.map { it.toData() } },
                paged = request.paged
            )
        }
    }

    override suspend fun editService(request: EditServiceRequest, account: AuthorizedAccount): EditServiceResponse {
        return transactional.withSuspendedTransaction {
            val info = with(converter) { request.data.toNewEntity() }
            val newService = with(servicesRepository) { updateServiceInfo(request.serviceId, info) }
            EditServiceResponse(
                service = with(converter) { newService.toData() }
            )
        }
    }


    override suspend fun createService(
        request: CreateServiceRequest,
        account: AuthorizedAccount,
    ): CreateServiceResponse {
        return transactional.withSuspendedTransaction {
            val data = request.data
            val service = with(servicesRepository) {
                createService(
                    ServiceInfoEntity(
                        title = data.title,
                        description = data.description,
                    ), storageId = request.storageId
                )
            }
            CreateServiceResponse(
                service = with(converter) { service.toData() }
            )
        }
    }

    override suspend fun getServiceById(
        request: GetServiceByIdRequest,
        account: AuthorizedAccount,
    ): GetServiceByIdResponse {
        return transactional.withSuspendedTransaction {
            val service = with(servicesRepository) { findServiceById(request.serviceId) }
            GetServiceByIdResponse(
                service = with(converter) { service.toData() }
            )
        }
    }

    override suspend fun addConfiguration(
        request: CreateConfigurationRequest,
        account: AuthorizedAccount,
    ): CreateConfigurationResponse {
        return transactional.withSuspendedTransaction {
            val newService = with(servicesRepository) {
                addConfiguration(
                    request.serviceId,
                    ServiceConfigurationEntityData(
                        title = request.data.title,
                        description = request.data.description,
                        cost = request.data.cost,
                        parameters = TypedServiceConfigurationEntity.WithAmountLimit(request.data.amount),
                    )
                )
            }
            CreateConfigurationResponse(
                service = with(converter) { newService.toData() }
            )
        }
    }

    override suspend fun deactivateConfiguration(
        request: DeactivateServiceConfigurationRequest,
        account: AuthorizedAccount,
    ): DeactivateServiceConfigurationResponse {
        TODO("Not yet implemented")
    }
}