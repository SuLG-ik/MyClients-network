package beauty.shafran.network.customers

import beauty.shafran.network.customers.data.*
import beauty.shafran.network.customers.executor.CustomersExecutor
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController
import java.io.File
import javax.validation.Valid
import kotlin.io.path.Path

@RequestMapping("/v1/customers")
@RestController
class Customers(
    private val executor: CustomersExecutor,
) {

    @RequestMapping(
        value = ["/searchCustomerByPhone"],
        method = [RequestMethod.GET],
        produces = [MediaType.APPLICATION_JSON_VALUE],
        consumes = [MediaType.APPLICATION_JSON_VALUE],
    )
    suspend fun searchCustomerByPhone(@Valid @RequestBody request: SearchCustomerByPhoneRequest): SearchCustomerByPhoneResponse {
        ResponseEntity.ok(File("fsdfds"))

        Path("fajfdsf").map {  }
        return executor.searchCustomerByPhone(request)
    }


    @RequestMapping(
        value = ["/restoreCustomer"],
        method = [RequestMethod.GET],
        produces = [MediaType.APPLICATION_JSON_VALUE],
        consumes = [MediaType.APPLICATION_JSON_VALUE],
    )
    suspend fun restoreCustomer(@Valid @RequestBody request: RestoreCustomerRequest) {
        return executor.restoreCustomer(request)
    }

    @RequestMapping(
        value = ["/createCustomer"],
        method = [RequestMethod.POST],
        produces = [MediaType.APPLICATION_JSON_VALUE],
        consumes = [MediaType.APPLICATION_JSON_VALUE],
    )
    suspend fun createCustomer(@Valid @RequestBody request: CreateCustomersRequest): CreateCustomersResponse {
        return executor.createCustomer(request)
    }

    @RequestMapping(
        value = ["/getCustomerById"],
        method = [RequestMethod.GET],
        produces = [MediaType.APPLICATION_JSON_VALUE],
        consumes = [MediaType.APPLICATION_JSON_VALUE],
    )
    suspend fun getCustomerById(@Valid @RequestBody request: GetCustomerByIdRequest): GetCustomerByIdResponse {
        return executor.getCustomerById(request)
    }



    @RequestMapping(
        value = ["/getCustomerByToken"],
        method = [RequestMethod.GET],
        produces = [MediaType.APPLICATION_JSON_VALUE],
        consumes = [MediaType.APPLICATION_JSON_VALUE],
    )
    suspend fun getCustomerByToken(@Valid @RequestBody request: GetCustomerByTokenRequest): GetCustomerByTokenResponse {
        return executor.getCustomerByToken(request)
    }



    @RequestMapping(
        value = ["/getAllCustomers"],
        method = [RequestMethod.GET],
        produces = [MediaType.APPLICATION_JSON_VALUE],
        consumes = [MediaType.APPLICATION_JSON_VALUE],
    )
    suspend fun getAllCustomers(@Valid @RequestBody request: GetAllCustomersRequest): GetAllCustomersResponse {
        return executor.getAllCustomers(request)
    }


    @RequestMapping(
        value = ["/createEmptyCustomers"],
        method = [RequestMethod.POST],
        produces = [MediaType.APPLICATION_JSON_VALUE],
        consumes = [MediaType.APPLICATION_JSON_VALUE],
    )
    suspend fun createEmptyCustomers(@Valid @RequestBody request: CreateEmptyCustomersRequest): CreateEmptyCustomersResponse {
        return executor.createEmptyCustomers(request)
    }

    @RequestMapping(
        value = ["/editCustomerData"],
        method = [RequestMethod.PATCH],
        produces = [MediaType.APPLICATION_JSON_VALUE],
        consumes = [MediaType.APPLICATION_JSON_VALUE],
    )
    suspend fun createCustomer(@Valid @RequestBody request: EditCustomerRequest): EditCustomerResponse {
        return executor.editCustomerData(request)
    }

}

//fun Route.configureCustomers() {
//    val customer: CustomersReducer by inject()
//    route("/customers") {
//        withApi {
//            get("/searchCustomerByPhone", customer.searchCustomerByPhone)
//            get("/restoreCustomer", customer.restoreCustomer)
//            post("/createCustomer", customer.createCustomer)
//            get("/getCustomerById", customer.getCustomerById)
//            get("/getCustomerByToken", customer.getCustomerByToken)
//            get("/getAllCustomers", customer.getAllCustomers)
//            post("/createEmptyCustomers", customer.createEmptyCustomers)
//            patch("/editCustomerData", customer.editCustomerData)
//        }
//    }
//}