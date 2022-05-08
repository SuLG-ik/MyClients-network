package beauty.shafran.network.customers.executor

import beauty.shafran.network.auth.data.AuthorizedAccount
import beauty.shafran.network.customers.data.*

interface CustomersExecutor {

    suspend fun searchCustomerByPhone(request: SearchCustomerByPhoneRequest, account: AuthorizedAccount): SearchCustomerByPhoneResponse
    suspend fun createCustomer(request: CreateCustomersRequest, account: AuthorizedAccount): CreateCustomerResponse
    suspend fun getCustomerById(request: GetCustomerByIdRequest, account: AuthorizedAccount): GetCustomerByIdResponse
    suspend fun getCustomerByToken(request: GetCustomerByTokenRequest, account: AuthorizedAccount): GetCustomerByTokenResponse
    suspend fun getAllCustomers(request: GetAllCustomersRequest, account: AuthorizedAccount): GetAllCustomersResponse
    suspend fun createEmptyCustomers(request: CreateEmptyCustomersRequest, account: AuthorizedAccount): CreateEmptyCustomersResponse
    suspend fun editCustomerData(request: EditCustomerRequest, account: AuthorizedAccount): EditCustomerDataResponse

}