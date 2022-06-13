package beauty.shafran.network

import io.ktor.http.*
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("access_deny")
class AccessDenied : NetworkException("AccessDeny") {
    override val httpStatusCode: HttpStatusCode
        get() = HttpStatusCode.Forbidden
}

@Serializable
@SerialName("bad_request")
class BadRequest: NetworkException("BadRequest") {
    override val httpStatusCode: HttpStatusCode
        get() = HttpStatusCode.BadRequest
}

@Serializable
abstract class NetworkException(
    override val message: String = ""
) : Exception() {
    abstract val httpStatusCode: HttpStatusCode
}
