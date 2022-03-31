package beauty.shafran.network.services

import beauty.shafran.network.ShafranNetworkException
import io.ktor.http.*
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("service_not_exists")
class ServiceNotExists(
    val serviceId: String,
) : ShafranNetworkException() {

    override val httpStatusCode: HttpStatusCode
        get() = HttpStatusCode.NotFound

}

@Serializable
@SerialName("configuration_not_exists")
class ConfigurationNotExists(
    val configurationId: String,
) : ShafranNetworkException() {

    override val httpStatusCode: HttpStatusCode
        get() = HttpStatusCode.NotFound

}