package beauty.shafran.network.customers.validators

import beauty.shafran.network.customers.data.*
import beauty.shafran.network.validation.validateAndThrow
import jakarta.validation.Validator

class CustomersValidatorImpl(private val validator: Validator) : CustomersValidator {


    override fun searchCustomerByPhone(request: SearchCustomerByPhoneRequest): SearchCustomerByPhoneRequest {
        return validator.validateAndThrow(request)
    }

    override fun restoreCustomer(request: RestoreCustomerRequest): RestoreCustomerRequest {
        return validator.validateAndThrow(request)
    }

    override fun createCustomer(request: CreateCustomersRequest): CreateCustomersRequest {
        return validator.validateAndThrow(request.trim())
    }

    override fun getCustomerById(request: GetCustomerByIdRequest): GetCustomerByIdRequest {
        return validator.validateAndThrow(request)
    }

    override fun getCustomerByToken(request: GetCustomerByTokenRequest): GetCustomerByTokenRequest {
        return validator.validateAndThrow(request)
    }

    override fun getAllCustomers(request: GetAllCustomersRequest): GetAllCustomersRequest {
        return validator.validateAndThrow(request)
    }

    override fun createEmptyCustomers(request: CreateEmptyCustomersRequest): CreateEmptyCustomersRequest {
        return validator.validateAndThrow(request)
    }

    override fun editCustomerData(request: EditCustomerRequest): EditCustomerRequest {
        return validator.validateAndThrow(request.trim())
    }
}