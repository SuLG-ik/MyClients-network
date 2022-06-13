package beauty.shafran.network.auth

import beauty.shafran.network.KtorAuthAuthorized
import beauty.shafran.network.auth.encoder.DelegatingPasswordEncoder
import beauty.shafran.network.auth.encoder.JBcryptPasswordEncoder
import beauty.shafran.network.auth.encoder.PasswordEncoder
import beauty.shafran.network.auth.jwt.JWTAuthModule
import beauty.shafran.network.auth.jwt.JWTAuthenticationConfig
import beauty.shafran.network.auth.repository.AuthPasswordRepository
import beauty.shafran.network.auth.repository.AuthPasswordRepositoryImpl
import beauty.shafran.network.auth.route.AuthRouter
import beauty.shafran.network.auth.route.AuthRouterImpl
import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm
import org.jetbrains.exposed.sql.SchemaUtils
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val AuthModule = module {
    includes(JWTAuthModule)
    singleOf(::KtorAuthAuthorized) bind Authorized::class
    singleOf(::AuthRouterImpl) bind AuthRouter::class
    singleOf(::passwordEncoder)
    singleOf(::AuthPasswordRepositoryImpl) bind AuthPasswordRepository::class
}


private fun passwordEncoder(): PasswordEncoder {
    return DelegatingPasswordEncoder("bcrypt", mapOf("bcrypt" to JBcryptPasswordEncoder()))
}