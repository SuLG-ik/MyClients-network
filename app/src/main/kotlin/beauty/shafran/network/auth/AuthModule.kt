package beauty.shafran.network.auth

import at.favre.lib.crypto.bcrypt.BCrypt
import beauty.shafran.network.auth.executor.AuthenticationExecutor
import beauty.shafran.network.auth.executor.AuthenticationExecutorImpl
import beauty.shafran.network.auth.jwt.JwtAuthenticationValidator
import beauty.shafran.network.auth.jwt.JwtConfig
import beauty.shafran.network.auth.repository.AccountSessionsRepository
import beauty.shafran.network.auth.repository.PermissionsRepository
import beauty.shafran.network.auth.repository.PostgresAccountSessionsRepository
import beauty.shafran.network.auth.repository.PostgresPermissionsRepository
import beauty.shafran.network.auth.token.JwtTokenAuthService
import beauty.shafran.network.auth.token.TokenAuthService
import io.ktor.server.config.*
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.bind
import org.koin.dsl.module

val AuthModule = module {
    factoryOf(::apiKey)
    factoryOf(::hasher)
    factoryOf(::verifier)
    factoryOf(::jwtConfig)
    factoryOf(::AlwaysValid) bind AuthorizationValidator::class
    factoryOf(::JwtAuthenticationValidator) bind AuthenticationValidator::class
    factoryOf(::PostgresAccountSessionsRepository) bind AccountSessionsRepository::class
    factoryOf(::PostgresPermissionsRepository) bind PermissionsRepository::class
    factoryOf(::AuthenticationExecutorImpl) bind AuthenticationExecutor::class
    factoryOf(::JwtTokenAuthService) bind TokenAuthService::class
}

private fun apiKey(config: ApplicationConfig): ApiKeyConfig {
    return ApiKeyConfig(
        header = config.tryGetString("security.api.key.header")!!,
        key = config.tryGetString("security.api.key.key")!!,
        authHeader = config.tryGetString("security.api.auth.header")!!,
    )
}

private fun hasher(): BCrypt.Hasher {
    return BCrypt.withDefaults()
}

private fun verifier(): BCrypt.Verifyer {
    return BCrypt.verifyer()
}

private fun jwtConfig(config: ApplicationConfig): JwtConfig {
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