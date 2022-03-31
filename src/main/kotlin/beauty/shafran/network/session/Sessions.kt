package beauty.shafran.network.session

import beauty.shafran.network.api.withApi
import inject
import io.ktor.server.routing.*

fun Route.configureSessions() {
    val reducer by inject<SessionsReducer>()
    route("/sessions") {
        withApi {
            get("/getSessionUsagesHistory", reducer.getSessionUsagesHistory)
            get("/getServiceSessionsForCustomer", reducer.getServiceSessionsForCustomer)
            put("/useServiceSession", reducer.useServiceSession)
            post("/createServiceSessionsForCustomer", reducer.createServiceSessionsForCustomer)
        }
    }
}