package beauty.shafran.network.auth.jwt

@ru.sulgik.config.ConfigurationProperties("ktor.security.api.jwt")
internal class JWTAuthenticationConfig(
    @ru.sulgik.config.PropertySuffix("issuer")
    val issuer: String,
    @ru.sulgik.config.PropertySuffix("audience")
    val audience: String,
    @ru.sulgik.config.PropertySuffix("path")
    val path: String,
    @ru.sulgik.config.PropertySuffix("alias")
    val alias: String,
    @ru.sulgik.config.PropertySuffix("password")
    val password: String,
    @ru.sulgik.config.PropertySuffix("storePassword")
    val storePassword: String,
    @ru.sulgik.config.PropertySuffix("accessTokenExpiresAfter")
    val accessTokenExpiresAfter: Long,
    @ru.sulgik.config.PropertySuffix("realm")
    val realm: String,
)