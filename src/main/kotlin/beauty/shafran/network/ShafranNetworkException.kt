package beauty.shafran.network

import beauty.shafran.network.customers.exceptions.*
import beauty.shafran.network.employees.EmployeeNotExistsWithId
import beauty.shafran.network.services.ConfigurationNotExists
import beauty.shafran.network.services.ServiceNotExists
import beauty.shafran.network.session.SessionNotExists
import beauty.shafran.network.session.SessionOveruseException
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.util.reflect.*
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.modules.SerializersModuleBuilder
import kotlinx.serialization.modules.polymorphic
import kotlinx.serialization.modules.subclass

@Serializable
abstract class ShafranNetworkException : Exception() {

    abstract val httpStatusCode: HttpStatusCode

}

@Serializable
@SerialName("bad_request_data")
class BadRequest() : ShafranNetworkException() {
    override val httpStatusCode: HttpStatusCode get() = HttpStatusCode.BadRequest
}

@Serializable
@SerialName("illegal_id")
class IllegalId(val field: String, val id: String) : ShafranNetworkException() {
    override val httpStatusCode: HttpStatusCode get() = HttpStatusCode.BadRequest
}


fun SerializersModuleBuilder.exceptionsSerializationModule() {
    polymorphic(ShafranNetworkException::class) {
        subclass(BadRequest::class)
        subclass(CustomerNotExists::class)
        subclass(CardNotExistsWithId::class)
        subclass(CardNotExistsForCustomer::class)
        subclass(IllegalCardToken::class)
        subclass(CustomerNotActivated::class)
        subclass(IllegalPhoneNumber::class)
        subclass(EmployeeNotExistsWithId::class)
        subclass(ServiceNotExists::class)
        subclass(ConfigurationNotExists::class)
        subclass(SessionOveruseException::class)
        subclass(SessionNotExists::class)
        subclass(IllegalId::class)
    }
}

suspend inline fun <reified T : Any> ApplicationCall.receiveOrThrow(): T = try {
    receive(typeInfo<T>())
} catch (e: Exception) {
    throw BadRequest()
}