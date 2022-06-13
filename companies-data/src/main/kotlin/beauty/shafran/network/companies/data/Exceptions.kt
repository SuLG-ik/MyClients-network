package beauty.shafran.network.companies.data

import beauty.shafran.network.NetworkException
import io.ktor.http.*
import kotlinx.serialization.Serializable

@Serializable
class AccountNotMemberOfCompany() : NetworkException() {
    override val httpStatusCode: HttpStatusCode
        get() = HttpStatusCode.BadRequest
}
