package beauty.shafran.network.customers.exceptions

import io.ktor.http.*
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
sealed class CustomersException : Exception() {

    abstract val httpCode: Int

    @Serializable
    @SerialName("customer_not_found")
    class CustomerNotFoundException(val customerId: String) : CustomersException() {
        override val httpCode: Int = HttpStatusCode.NotFound.value
    }

    @Serializable
    @SerialName("card_not_found")
    class CardNotFoundWithIdException(val cardId: String) : CustomersException() {
        override val httpCode: Int = HttpStatusCode.NotFound.value
    }

    @Serializable
    @SerialName("card_not_found_with_customer")
    class CardNotFoundWithCustomerException(val customerId: String) : CustomersException() {
        override val httpCode: Int = HttpStatusCode.NotFound.value
    }

    @Serializable
    @SerialName("card_illegal")
    class IllegalCardTokenException(val cardToken: String) : CustomersException() {
        override val httpCode: Int = HttpStatusCode.BadRequest.value
    }

    @Serializable
    @SerialName("customer_not_activated")
    class CustomerNotActivated(val customerId: String) : CustomersException() {
        override val httpCode: Int = HttpStatusCode.NotFound.value
    }

}