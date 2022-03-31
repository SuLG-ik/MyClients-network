package beauty.shafran.network.session

import beauty.shafran.network.ShafranNetworkException
import io.ktor.http.*
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("session_overuse")
class SessionOveruseException(
    val sessionId: String,
) : ShafranNetworkException() {

    override val httpStatusCode: HttpStatusCode
        get() = HttpStatusCode.Conflict

}


@Serializable
@SerialName("session_not_exists")
class SessionNotExists(
    val sessionId: String,
) : ShafranNetworkException() {

    override val httpStatusCode: HttpStatusCode
        get() = HttpStatusCode.NotFound

}
