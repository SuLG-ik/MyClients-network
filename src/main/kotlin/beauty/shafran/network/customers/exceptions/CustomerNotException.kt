package beauty.shafran.network.customers.exceptions

import beauty.shafran.network.ShafranNetworkException
import io.ktor.http.*
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("customer_not_exists")
class CustomerNotExists(val customerId: String) : ShafranNetworkException() {
    override val httpStatusCode: HttpStatusCode
        get() = HttpStatusCode.NotFound
}

@Serializable
@SerialName("card_not_exists")
class CardNotExistsWithId(val cardId: String) : ShafranNetworkException() {
    override val httpStatusCode: HttpStatusCode
        get() = HttpStatusCode.NotFound
}

@Serializable
@SerialName("card_not_exist_with_customer")
class CardNotExistsForCustomer(val customerId: String) : ShafranNetworkException() {
    override val httpStatusCode: HttpStatusCode
        get() = HttpStatusCode.NotFound
}

@Serializable
@SerialName("card_illegal")
class IllegalCardToken(val cardToken: String) : ShafranNetworkException() {
    override val httpStatusCode: HttpStatusCode
        get() = HttpStatusCode.BadRequest
}

@Serializable
@SerialName("customer_not_activated")
class CustomerNotActivated(val customerId: String) : ShafranNetworkException() {
    override val httpStatusCode: HttpStatusCode
        get() = HttpStatusCode.NotFound
}

@Serializable
@SerialName("illegal_phone_number")
class IllegalPhoneNumber(val number: String) : ShafranNetworkException() {
    override val httpStatusCode: HttpStatusCode
        get() = HttpStatusCode.NotFound
}

@Serializable
@SerialName("illegal_request")
class IllegalRequest(val fieldName: String, override val message: String) : ShafranNetworkException() {
    override val httpStatusCode: HttpStatusCode
        get() = HttpStatusCode.BadRequest
}