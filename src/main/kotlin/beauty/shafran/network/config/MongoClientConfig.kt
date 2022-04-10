package beauty.shafran.network.config

import com.mongodb.ConnectionString
import com.mongodb.MongoClientSettings
import com.mongodb.MongoCredential
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

@ConfigurationProperties("kmongo")
@ConstructorBinding
class MongoClientConfig(
    val url: String,
    val database: String,
    val username: String,
    val password: String,
    val authDatabase: String,
)

fun MongoClientConfig.toSettings(): MongoClientSettings {
    return MongoClientSettings.builder()
        .applyConnectionString(ConnectionString(url))
        .credential(MongoCredential.createCredential(username, authDatabase, password.toCharArray()))
        .build()
}
