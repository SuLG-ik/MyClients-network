package beauty.shafran.network.config

import com.mongodb.ConnectionString
import com.mongodb.MongoClientSettings
import com.mongodb.MongoCredential

class MongoClientConfig(
    val url: String = System.getenv("DATABASE_URL"),
    val database: String = System.getenv("DATABASE_NAME"),
    val username: String = System.getenv("DATABASE_USERNAME"),
    val password: String = System.getenv("DATABASE_PASSWORD"),
    val authDatabase: String = System.getenv("DATABASE_AUTH_NAME"),
)

fun MongoClientConfig.toSettings(): MongoClientSettings {
    return MongoClientSettings.builder()
        .applyConnectionString(ConnectionString(url))
        .credential(MongoCredential.createCredential(username, authDatabase, password.toCharArray()))
        .build()
}
