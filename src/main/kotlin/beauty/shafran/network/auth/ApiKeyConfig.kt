package beauty.shafran.network.auth

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

@ConfigurationProperties("security.api")
@ConstructorBinding
class ApiKeyConfig(
    val header: String,
    val key: String,
    val authHeader: String,
)