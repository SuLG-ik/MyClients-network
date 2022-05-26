package beauty.shafran.network.admin.company

import beauty.shafran.network.admin.company.executor.AdminCompanyExecutor
import beauty.shafran.network.utils.callWrapper
import io.ktor.server.routing.*

fun Route.adminCompanyRoute(
    executor: AdminCompanyExecutor,
) {
    post("/createCompany", callWrapper(executor::createCompany))
}