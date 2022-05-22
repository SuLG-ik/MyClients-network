package beauty.shafran.network

data class DatabaseConfiguration(
    val url: String,
    val user: String,
    val password: String,
    val driver: String,
)