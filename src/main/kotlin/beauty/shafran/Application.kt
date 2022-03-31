package beauty.shafran

import io.ktor.network.tls.certificates.*
import io.ktor.server.netty.*
import java.io.File
import java.security.KeyStore

fun main(args: Array<String>) = EngineMain.main(args)

private fun loadKey(config: SslConfig): KeyStore {
    val keyStoreFile = File(config.file)
    return generateCertificate(
        file = keyStoreFile,
        keyAlias = config.alias,
        keyPassword = config.password,
        jksPassword = config.password,
    )
}

private fun loadConfig(): ServerConfig {
    return ServerConfig(
        port = System.getenv("SERVER_PORT").toInt(),
        ssl = loadSslConfig(),
    )
}

private fun loadSslConfig(): SslConfig? {
    if (System.getenv("SSL_ENABLE").toBooleanStrictOrNull() == false)
        return null
    return SslConfig(
        file = System.getenv("SSL_PATH"),
        alias = System.getenv("SSL_ALIAS"),
        password = System.getenv("SSL_PASSWORD")
    )
}

data class ServerConfig(
    val port: Int,
    val ssl: SslConfig?,
)

data class SslConfig(
    val file: String,
    val alias: String,
    val password: String,
)