package beauty.shafran.network.auth

import at.favre.lib.crypto.bcrypt.BCrypt
import beauty.shafran.network.auth.jwt.JwtConfig
import io.ktor.server.config.*
import org.koin.core.annotation.Module
import org.koin.core.annotation.Single

@Module
class AuthModule {

    @Single
    fun apiKey(config: ApplicationConfig): ApiKeyConfig {
        return ApiKeyConfig(
            header = config.tryGetString("security.api.key.header")!!,
            key = config.tryGetString("security.api.key.key")!!,
            authHeader = config.tryGetString("security.api.auth.header")!!,
        )
    }

    @Single
    fun hasher(): BCrypt.Hasher {
        return BCrypt.withDefaults()
    }

    @Single
    fun verifier(): BCrypt.Verifyer {
        return BCrypt.verifyer()
    }

    @Single
    fun jwtConfig(config: ApplicationConfig): JwtConfig {
        return JwtConfig(
            issuer = config.tryGetString("ktor.security.api.jwt.issuer")!!,
            audience = config.tryGetString("ktor.security.api.jwt.audience")!!,
            path = config.tryGetString("ktor.security.api.jwt.path")!!,
            alias = config.tryGetString("ktor.security.api.jwt.alias")!!,
            password = config.tryGetString("ktor.security.api.jwt.password")!!,
            storePassword = config.tryGetString("ktor.security.api.jwt.storePassword")!!,
            realm = config.tryGetString("ktor.security.api.jwt.realm")!!,
            accessTokenExpiresAfter = config.tryGetString("ktor.security.api.jwt.accessTokenExpiresAfter")!!.toLong(),
            refreshTokenExpiresAfter = config.tryGetString("ktor.security.api.jwt.refreshTokenExpiresAfter")!!.toLong(),
        )
    }


}