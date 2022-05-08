package beauty.shafran.network.companies

import beauty.shafran.network.companies.executor.CompaniesExecutor
import beauty.shafran.network.utils.callWrapper
import io.ktor.server.routing.*

fun Route.companiesRoute(
    executor: CompaniesExecutor,
) {
    post("/createCompany", callWrapper(executor::createCompany))
    get("/getAvailableCompaniesList", callWrapper(executor::getAvailableCompaniesList))
}