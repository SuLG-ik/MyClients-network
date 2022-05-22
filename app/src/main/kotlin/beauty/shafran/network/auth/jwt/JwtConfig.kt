package beauty.shafran.network.auth.jwt

class JwtConfig(
    val issuer: String,
    val audience: String,
    val path: String,
    val alias: String,
    val password: String,
    val storePassword: String,
    val accessTokenExpiresAfter: Long,
    val refreshTokenExpiresAfter: Long,
    val realm: String
)
