package beauty.shafran.network.auth.jwt

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

@ConfigurationProperties("security.api.jwt")
@ConstructorBinding
internal class JWTAuthenticationConfig(
    val issuer: String,
    val audience: String,
    val path: String,
    val alias: String,
    val password: String,
    val storePassword: String,
    val accessTokenExpiresAfter: Long,
    val realm: String,
)