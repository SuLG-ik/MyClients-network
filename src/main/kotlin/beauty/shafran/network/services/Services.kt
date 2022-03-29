package beauty.shafran.network.services

import beauty.shafran.network.api.withApi
import inject
import io.ktor.server.routing.*

fun Route.configureServices() {
    val reducer by inject<ServicesReducer>()

    route("/services") {
        withApi {
            get("/getAllServices", reducer.getAllServices)
            get("/getServiceById", reducer.getServiceById)
            post("/createService", reducer.createService)
            put("/addConfiguration", reducer.addConfiguration)
            delete("/deactivateConfiguration", reducer.deactivateConfiguration)
        }
    }
}