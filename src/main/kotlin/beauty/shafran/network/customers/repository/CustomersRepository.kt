package beauty.shafran.network.customers.repository

import beauty.shafran.network.customers.data.*

interface CustomersRepository {

    suspend fun restoreCustomer(request: RestoreCustomerRequest)
    suspend fun createCustomer(request: CreateCustomersRequest) : CreateCustomersResponse
    suspend fun getCustomerById(request: GetCustomerByIdRequest) : GetCustomerByIdResponse
    suspend fun getCustomerByToken(request: GetCustomerByTokenRequest): GetCustomerByTokenResponse
    suspend fun getAllCustomers(request: GetAllCustomersRequest): GetAllCustomersResponse
    suspend fun createEmptyCustomers(request: CreateEmptyCustomersRequest): CreateEmptyCustomersResponse
    suspend fun editCustomerData(request: EditCustomerRequest): EditCustomerResponse

}