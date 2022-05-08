package beauty.shafran

import beauty.shafran.network.customers.entity.CustomerId
import beauty.shafran.network.validation.ValidationSubject
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.response.*
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerializationException

fun Application.exceptionsHandler() {
    install(StatusPages) {
        exception { call: ApplicationCall, cause: NetworkException ->
            this@exceptionsHandler.log.warn("Error during request", cause)
            call.respond(cause.httpStatusCode, cause)
        }
        exception { call: ApplicationCall, cause: SerializationException ->
            val response = BadRequest()
            this@exceptionsHandler.log.warn("Error during request", cause)
            call.respond(response.httpStatusCode, response)
        }
    }
}

@Serializable
sealed class NetworkException : Exception() {
    abstract val httpStatusCode: HttpStatusCode
}

@Serializable
@SerialName("bad_request_data")
class BadRequest : NetworkException() {
    override val httpStatusCode: HttpStatusCode
        get() = HttpStatusCode.BadRequest
}

@Serializable
@SerialName("illegal_id")
class IllegalId(val field: String, val id: String) : NetworkException() {
    override val httpStatusCode: HttpStatusCode
        get() = HttpStatusCode.BadRequest
}

@Serializable
@SerialName("customer_not_exists")
class CustomerNotExists(val customerId: CustomerId) : NetworkException() {
    override val httpStatusCode: HttpStatusCode
        get() = HttpStatusCode.NotFound
}

@Serializable
@SerialName("card_not_exists")
class CardNotExistsWithId(val cardId: String) : NetworkException() {
    override val httpStatusCode: HttpStatusCode
        get() = HttpStatusCode.NotFound
}

@Serializable
@SerialName("card_not_exist_with_customer")
class CardNotExistsForCustomer(val customerId: String) : NetworkException() {
    override val httpStatusCode: HttpStatusCode
        get() = HttpStatusCode.NotFound
}

@Serializable
@SerialName("card_illegal")
class IllegalCardToken(val cardToken: String) : NetworkException() {
    override val httpStatusCode: HttpStatusCode
        get() = HttpStatusCode.BadRequest
}

@Serializable
@SerialName("customer_not_activated")
class CustomerNotActivated(val customerId: String) : NetworkException() {
    override val httpStatusCode: HttpStatusCode
        get() = HttpStatusCode.NotFound
}

@Serializable
@SerialName("illegal_phone_number")
class IllegalPhoneNumber(val number: String) : NetworkException() {
    override val httpStatusCode: HttpStatusCode
        get() = HttpStatusCode.NotFound
}

@Serializable
@SerialName("employee_not_exists")
class EmployeeNotExistsWithId(
    val employeeId: String,
) : NetworkException() {
    override val httpStatusCode: HttpStatusCode
        get() = HttpStatusCode.NotFound
}

@Serializable
@SerialName("service_not_exists")
class ServiceNotExists(
    val serviceId: String,
) : NetworkException() {

    override val httpStatusCode: HttpStatusCode
        get() = HttpStatusCode.NotFound

}

@Serializable
@SerialName("configuration_not_exists")
class ConfigurationNotExists(
    val configurationId: String,
) : NetworkException() {
    override val httpStatusCode: HttpStatusCode
        get() = HttpStatusCode.NotFound
}

@Serializable
@SerialName("session_overuse")
class SessionOveruseException(
    val sessionId: String,
) : NetworkException() {

    override val httpStatusCode: HttpStatusCode
        get() = HttpStatusCode.Conflict

}


@Serializable
@SerialName("session_not_exists")
class SessionNotExists(
    val sessionId: String,
) : NetworkException() {
    override val httpStatusCode: HttpStatusCode
        get() = HttpStatusCode.NotFound
}

@Serializable
@SerialName("session_deactivated")
class SessionIsDeactivated(
    val sessionId: String,
) : NetworkException() {
    override val httpStatusCode: HttpStatusCode
        get() = HttpStatusCode.Conflict
}

@Serializable
@SerialName("session_used")
class SessionIsAlreadyUsed(
    val sessionId: String,
) : NetworkException() {
    override val httpStatusCode: HttpStatusCode
        get() = HttpStatusCode.Conflict
}

@Serializable
@SerialName("validation")
class ValidationException(
    val error: List<ValidationSubject>,
) : NetworkException() {
    override val httpStatusCode: HttpStatusCode
        get() = HttpStatusCode.BadRequest
}

@Serializable
@SerialName("business_not_exists")
class BusinessNotExists(
    val businessId: String,
) : NetworkException() {
    override val httpStatusCode: HttpStatusCode
        get() = HttpStatusCode.NotFound
}

@Serializable
@SerialName("account_not_exists")
class AccountNotExists(val accountId: String) : NetworkException() {
    override val httpStatusCode: HttpStatusCode
        get() = HttpStatusCode.NotFound
}


@Serializable
@SerialName("account_already_deactivated")
class AccountAlreadyDeactivated(val accountId: String) : NetworkException() {
    override val httpStatusCode: HttpStatusCode
        get() = HttpStatusCode.NotFound
}

@Serializable
@SerialName("account_already_exists")
class AccountAlreadyExists(val login: String) : NetworkException() {
    override val httpStatusCode: HttpStatusCode
        get() = HttpStatusCode.NotFound
}

@Serializable
@SerialName("account_illegal_credentials")
class AccountIllegalCredentials(val login: String) : NetworkException() {
    override val httpStatusCode: HttpStatusCode
        get() = HttpStatusCode.NotFound
}


@Serializable
@SerialName("token_illegal")
class IllegalToken() : NetworkException() {
    override val httpStatusCode: HttpStatusCode
        get() = HttpStatusCode.Forbidden
}

@Serializable
@SerialName("token_expired")
class TokenExpired() : NetworkException() {
    override val httpStatusCode: HttpStatusCode
        get() = HttpStatusCode.Unauthorized
}

@Serializable
@SerialName("account_session_not_exists")
class AccountSessionNotExists(val sessionId: String) : NetworkException() {
    override val httpStatusCode: HttpStatusCode
        get() = HttpStatusCode.NotFound
}

@Serializable
@SerialName("access_denied")
class AccessDenied() : NetworkException() {
    override val httpStatusCode: HttpStatusCode
        get() = HttpStatusCode.Forbidden
}