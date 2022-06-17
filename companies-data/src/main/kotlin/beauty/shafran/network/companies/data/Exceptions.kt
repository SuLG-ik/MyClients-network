package beauty.shafran.network.companies.data

import beauty.shafran.network.NetworkException
import io.ktor.http.*
import kotlinx.serialization.Serializable

@Serializable
class AccountNotMemberOfCompany() : NetworkException() {
    override val httpStatusCode: HttpStatusCode
        get() = HttpStatusCode.BadRequest
}

@Serializable
class PlacementNotInCompany : NetworkException() {
    override val httpStatusCode: HttpStatusCode
        get() = HttpStatusCode.Conflict

}

@Serializable
class CompanyPlacementNotExists() : NetworkException() {
    override val httpStatusCode: HttpStatusCode
        get() = HttpStatusCode.NotFound
}

@Serializable
class CompanyNotExists() : NetworkException() {
    override val httpStatusCode: HttpStatusCode
        get() = HttpStatusCode.NotFound
}
