package beauty.shafran.network.session

import beauty.shafran.network.session.executor.SessionsExecutor
import beauty.shafran.network.utils.callWrapper
import io.ktor.server.routing.*


fun Route.sessionsRoute(
    executor: SessionsExecutor,
) {
    get("/getSessionUsagesHistory", callWrapper(executor::getSessionUsagesHistory))
    get("/getSessionsStats", callWrapper(executor::getSessionsStats))
    get("/getAllServiceSessionsForCustomer", callWrapper(executor::getAllSessionsForCustomer))
    get("/getServiceSessionsIgnoreDeactivatedForCustomer", callWrapper(executor::getSessionsIgnoreDeactivatedForCustomer))
    put("/useServiceSession", callWrapper(executor::useSession))
    post("/createServiceSessionsForCustomer", callWrapper(executor::createSessionsForCustomer))
    delete("/deactivateServiceSession", callWrapper(executor::deactivateSession))
}