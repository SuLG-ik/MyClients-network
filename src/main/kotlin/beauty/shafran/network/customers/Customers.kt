package beauty.shafran.network.customers

import beauty.shafran.network.customers.executor.CustomersExecutor
import beauty.shafran.network.utils.callWrapper
import io.ktor.server.routing.*

fun Route.customersRoute(
    executor: CustomersExecutor,
) {
    get("/searchCustomerByPhone", callWrapper(executor::searchCustomerByPhone))
    post("/createCustomer", callWrapper(executor::createCustomer))
    get("/getCustomerById", callWrapper(executor::getCustomerById))
    get("/getCustomerByToken", callWrapper(executor::getCustomerByToken))
    post("/createEmptyCustomers", callWrapper(executor::createEmptyCustomers))
    patch("/editCustomerData", callWrapper(executor::editCustomerData))
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