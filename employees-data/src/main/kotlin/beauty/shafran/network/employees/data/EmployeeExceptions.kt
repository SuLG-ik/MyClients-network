package beauty.shafran.network.employees.data

import beauty.shafran.network.NetworkException
import io.ktor.http.*
import kotlinx.serialization.Serializable

@Serializable
class EmployeeNotExists : NetworkException() {
    override val httpStatusCode: HttpStatusCode
        get() = HttpStatusCode.NotFound
}

@Serializable
class EmployeeAlreadyInCompanyPlacement: NetworkException() {
    override val httpStatusCode: HttpStatusCode
        get() = HttpStatusCode.Conflict
}