package beauty.shafran.network.employees

import beauty.shafran.network.ShafranNetworkException
import io.ktor.http.*
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("employee_not_exists")
class EmployeeNotExistsWithId(
    val employeeId: String,
) : ShafranNetworkException() {
    override val httpStatusCode: HttpStatusCode
        get() = HttpStatusCode.NotFound
}