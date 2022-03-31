package beauty.shafran.network.services.validators

import beauty.shafran.network.services.data.*
import beauty.shafran.network.validation.validateAndThrow
import jakarta.validation.Validator

class ServicesValidatorImpl(private val validator: Validator) : ServicesValidator {
    override suspend fun getServices(request: GetAllServicesRequest): GetAllServicesRequest {
        return validator.validateAndThrow(request)
    }

    override suspend fun createService(request: CreateServiceRequest): CreateServiceRequest {
        return validator.validateAndThrow(request.trim())
    }

    override suspend fun getServiceById(request: GetServiceByIdRequest): GetServiceByIdRequest {
        return validator.validateAndThrow(request)
    }

    override suspend fun addConfiguration(request: CreateConfigurationRequest): CreateConfigurationRequest {
        return validator.validateAndThrow(request.trim())
    }

    override suspend fun deactivateConfiguration(request: DeactivateServiceConfigurationRequest): DeactivateServiceConfigurationRequest {
        return validator.validateAndThrow(request.trim())
    }
}