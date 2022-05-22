package beauty.shafran.network.companies

import beauty.shafran.network.companies.executor.CompaniesExecutor
import beauty.shafran.network.utils.callWrapper
import io.ktor.server.routing.*

fun Route.companiesRoute(
    executor: CompaniesExecutor,
) {
    get("/getAvailableCompaniesList", callWrapper(executor::getAvailableCompaniesList))
}