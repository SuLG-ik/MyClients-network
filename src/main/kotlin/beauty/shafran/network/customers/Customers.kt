package beauty.shafran.network.customers

import beauty.shafran.network.api.withApi
import inject
import io.ktor.server.routing.*


fun Route.configureCustomers() {
    val customer: CustomersReducer by inject()
    route("/customers") {
        withApi {
            get("/searchCustomerByPhone", customer.searchCustomerByPhone)
            get("/restoreCustomer", customer.restoreCustomer)
            post("/createCustomer", customer.createCustomer)
            get("/getCustomerById", customer.getCustomerById)
            get("/getCustomerByToken", customer.getCustomerByToken)
            get("/getAllCustomers", customer.getAllCustomers)
            post("/createEmptyCustomers", customer.createEmptyCustomers)
            patch("/editCustomerData", customer.editCustomerData)
        }
    }
}