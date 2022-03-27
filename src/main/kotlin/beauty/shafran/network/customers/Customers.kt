package beauty.shafran.network.customers

import io.ktor.routing.*
import org.koin.ktor.ext.inject

fun Routing.configureCustomers() {
    val customer: CustomersReducer by inject()
    route("/customers") {
        get("/restoreCustomer", customer.restoreCustomer)
        post("/createCustomer", customer.createCustomer)
        get("/getCustomerById", customer.getCustomerById)
        get("/getCustomerByToken", customer.getCustomerByToken)
        get("/getAllCustomers", customer.getAllCustomers)
        post("/createEmptyCustomers", customer.createEmptyCustomers)
        patch("/editCustomerData", customer.editCustomerData)
    }
}