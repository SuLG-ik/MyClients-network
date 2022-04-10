package beauty.shafran.network

import beauty.shafran.network.validation.ValidationSubject
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler
import javax.servlet.http.HttpServletRequest


@ControllerAdvice
class ExceptionHandler : ResponseEntityExceptionHandler() {

    @ResponseBody
    @ExceptionHandler(ShafranNetworkException::class)
    fun handleShafranNetwork(
        req: HttpServletRequest,
        exception: ShafranNetworkException,
    ): ResponseEntity<ShafranNetworkException> {
        return ResponseEntity(exception, exception.httpStatusCode)
    }

}

@Serializable
sealed class ShafranNetworkException : Exception() {
    abstract val httpStatusCode: HttpStatus
}

@Serializable
@SerialName("bad_request_data")
class BadRequest() : ShafranNetworkException() {
    override val httpStatusCode: HttpStatus get() = HttpStatus.BAD_REQUEST
}

@Serializable
@SerialName("illegal_id")
class IllegalId(val field: String, val id: String) : ShafranNetworkException() {
    override val httpStatusCode: HttpStatus get() = HttpStatus.BAD_REQUEST
}

@Serializable
@SerialName("customer_not_exists")
class CustomerNotExists(val customerId: String) : ShafranNetworkException() {
    override val httpStatusCode: HttpStatus
        get() = HttpStatus.NOT_FOUND
}

@Serializable
@SerialName("card_not_exists")
class CardNotExistsWithId(val cardId: String) : ShafranNetworkException() {
    override val httpStatusCode: HttpStatus
        get() = HttpStatus.NOT_FOUND
}

@Serializable
@SerialName("card_not_exist_with_customer")
class CardNotExistsForCustomer(val customerId: String) : ShafranNetworkException() {
    override val httpStatusCode: HttpStatus
        get() = HttpStatus.NOT_FOUND
}

@Serializable
@SerialName("card_illegal")
class IllegalCardToken(val cardToken: String) : ShafranNetworkException() {
    override val httpStatusCode: HttpStatus
        get() = HttpStatus.BAD_REQUEST
}

@Serializable
@SerialName("customer_not_activated")
class CustomerNotActivated(val customerId: String) : ShafranNetworkException() {
    override val httpStatusCode: HttpStatus
        get() = HttpStatus.NOT_FOUND
}

@Serializable
@SerialName("illegal_phone_number")
class IllegalPhoneNumber(val number: String) : ShafranNetworkException() {
    override val httpStatusCode: HttpStatus
        get() = HttpStatus.NOT_FOUND
}

@Serializable
@SerialName("illegal_request")
class IllegalRequest(val fieldName: String, override val message: String) : ShafranNetworkException() {
    override val httpStatusCode: HttpStatus
        get() = HttpStatus.BAD_REQUEST
}

@Serializable
@SerialName("employee_not_exists")
class EmployeeNotExistsWithId(
    val employeeId: String,
) : ShafranNetworkException() {
    override val httpStatusCode: HttpStatus
        get() = HttpStatus.NOT_FOUND
}

@Serializable
@SerialName("service_not_exists")
class ServiceNotExists(
    val serviceId: String,
) : ShafranNetworkException() {

    override val httpStatusCode: HttpStatus
        get() = HttpStatus.NOT_FOUND

}

@Serializable
@SerialName("configuration_not_exists")
class ConfigurationNotExists(
    val configurationId: String,
) : ShafranNetworkException() {
    override val httpStatusCode: HttpStatus
        get() = HttpStatus.NOT_FOUND
}

@Serializable
@SerialName("session_overuse")
class SessionOveruseException(
    val sessionId: String,
) : ShafranNetworkException() {

    override val httpStatusCode: HttpStatus
        get() = HttpStatus.CONFLICT

}




@Serializable
@SerialName("session_not_exists")
class SessionNotExists(
    val sessionId: String,
) : ShafranNetworkException() {
    override val httpStatusCode: HttpStatus
        get() = HttpStatus.NOT_FOUND
}

@Serializable
@SerialName("session_deactivated")
class SessionIsDeactivated(
    val sessionId: String,
) : ShafranNetworkException() {
    override val httpStatusCode: HttpStatus
        get() = HttpStatus.CONFLICT
}

@Serializable
@SerialName("session_used")
class SessionIsAlreadyUsed(
    val sessionId: String,
) : ShafranNetworkException() {
    override val httpStatusCode: HttpStatus
        get() = HttpStatus.CONFLICT
}

@Serializable
class ValidationException(
    val error: List<ValidationSubject>,
) : ShafranNetworkException() {
    override val httpStatusCode: HttpStatus
        get() = HttpStatus.BAD_REQUEST
}

