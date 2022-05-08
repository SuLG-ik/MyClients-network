package beauty.shafran.network.auth

class ApiKeyConfig(
    val header: String = System.getenv("API_HEADER"),
    val key: String = System.getenv("API_KEY"),
    val authHeader: String = System.getenv("API_AUTH_HEADER"),
)