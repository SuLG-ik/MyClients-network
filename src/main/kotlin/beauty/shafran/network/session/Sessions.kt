package beauty.shafran.network.session

import io.ktor.routing.*
import org.koin.ktor.ext.inject

fun Routing.configureSessions() {
    val reducer by inject<SessionsReducer>()
    route("/sessions") {
        get("/getServiceSessionsForCustomer", reducer.getServiceSessionsForCustomer)
        put("/useServiceSession", reducer.useServiceSession)
        post("/createServiceSessionsForCustomer", reducer.createServiceSessionsForCustomer)
    }
}