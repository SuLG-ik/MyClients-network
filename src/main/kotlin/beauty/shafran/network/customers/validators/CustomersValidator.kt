package beauty.shafran.network.customers.validators

import beauty.shafran.network.customers.data.*

interface CustomersValidator {

    fun searchCustomerByPhone(request: SearchCustomerByPhoneRequest): SearchCustomerByPhoneRequest
    fun restoreCustomer(request: RestoreCustomerRequest): RestoreCustomerRequest
    fun createCustomer(request: CreateCustomersRequest): CreateCustomersRequest
    fun getCustomerById(request: GetCustomerByIdRequest): GetCustomerByIdRequest
    fun getCustomerByToken(request: GetCustomerByTokenRequest): GetCustomerByTokenRequest
    fun getAllCustomers(request: GetAllCustomersRequest): GetAllCustomersRequest
    fun createEmptyCustomers(request: CreateEmptyCustomersRequest): CreateEmptyCustomersRequest
    fun editCustomerData(request: EditCustomerRequest): EditCustomerRequest

}