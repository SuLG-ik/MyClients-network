package beauty.shafran.network.auth.jwt

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

@ConfigurationProperties("jwt.keystore")
@ConstructorBinding
class JwtConfig(
    val issuer: String,
    val path: String,
    val alias: String,
    val password: String,
    val storePassword: String,
)
