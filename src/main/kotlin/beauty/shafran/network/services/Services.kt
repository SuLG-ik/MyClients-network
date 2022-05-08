package beauty.shafran.network.services

import beauty.shafran.network.services.executor.ServicesExecutor
import beauty.shafran.network.utils.callWrapper
import io.ktor.server.routing.*

fun Route.servicesRoute(
    executor: ServicesExecutor,
) {
    get("/getAllServices", callWrapper(executor::getServices))
    get("/getServiceById", callWrapper(executor::getServiceById))
    post("/createService", callWrapper(executor::createService))
    patch("/editService", callWrapper(executor::editService))
    put("/createConfiguration", callWrapper(executor::addConfiguration))
    post("/createService", callWrapper(executor::createService))
    delete("/deactivateConfiguration", callWrapper(executor::deactivateConfiguration))
}