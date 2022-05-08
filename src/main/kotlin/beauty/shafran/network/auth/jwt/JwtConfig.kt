package beauty.shafran.network.auth.jwt

class JwtConfig(
    val issuer: String = System.getenv("JWT_ISSUER"),
    val audience: String = System.getenv("JWT_AUDIENCE"),
    val path: String = System.getenv("JWT_STORE"),
    val alias: String = System.getenv("JWT_ALIAS"),
    val password: String = System.getenv("JWT_PASSWORD"),
    val storePassword: String = System.getenv("JWT_STORE_PASSWORD"),
    val realm: String = System.getenv("JWT_REALM")
)
