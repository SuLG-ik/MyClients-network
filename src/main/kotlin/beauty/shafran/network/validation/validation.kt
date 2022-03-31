package beauty.shafran.network.validation

import beauty.shafran.network.ShafranNetworkException
import io.ktor.http.*
import jakarta.validation.Validator
import kotlinx.serialization.Serializable

fun <T> Validator.validateAndThrow(data: T): T {
    val valid = validate(data)
    if (valid.isNotEmpty()) {
        throw ValidationException(
            error = valid.map {
                ValidationSubject(
                    message = it.message,
                )
            }
        )
    }
    return data
}

@Serializable
class ValidationException(
    val error: List<ValidationSubject>,
) : ShafranNetworkException() {
    override val httpStatusCode: HttpStatusCode
        get() = HttpStatusCode.BadRequest
}

@Serializable
class ValidationSubject(
    val message: String,
)