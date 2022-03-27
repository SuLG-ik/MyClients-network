package beauty.shafran.network.services

import io.ktor.routing.*
import org.koin.ktor.ext.inject

fun Routing.configureServices() {
    val reducer by inject<ServicesReducer>()

    route("/services") {
        get("/getAllServices", reducer.getAllServices)
        get("/getServiceById", reducer.getServiceById)
        post("/createService", reducer.createService)
        put("/addConfiguration", reducer.addConfiguration)
        delete("/deactivateConfiguration", reducer.deactivateConfiguration)
    }
}