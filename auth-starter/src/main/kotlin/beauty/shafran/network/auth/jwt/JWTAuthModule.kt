package beauty.shafran.network.auth.jwt

import beauty.shafran.network.auth.token.AuthTokenService
import beauty.shafran.network.auth.token.AuthTokenServiceImpl
import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.interfaces.JWTVerifier
import io.ktor.server.config.*
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.binds
import org.koin.dsl.module
import java.io.File
import java.security.KeyStore
import java.security.cert.Certificate
import java.security.interfaces.RSAPrivateKey
import java.security.interfaces.RSAPublicKey

internal val JWTAuthModule = module {
    single { get<ApplicationConfig>().toJWTAuthenticationConfig() }
    singleOf(::SavedJWTAuthValidator) bind JWTAuthentication::class
    singleOf(::AuthTokenServiceImpl) bind AuthTokenService::class
    singleOf(::keystore)
    singleOf(::cert)
    singleOf(::public)
    singleOf(::private)
    singleOf(::algorithm)
    singleOf(::defaultVerifier)
    singleOf(::VerifierImpl) binds arrayOf(Verifier::class, JWTVerifier::class)
}

private fun keystore(config: JWTAuthenticationConfig): KeyStore {
    return KeyStore.getInstance(File(config.path), config.storePassword.toCharArray())
}

private fun cert(keyStore: KeyStore, config: JWTAuthenticationConfig): Certificate {
    return keyStore.getCertificate(config.alias)
}

private fun public(certificate: Certificate): RSAPublicKey {
    return certificate.publicKey.let { if (it is RSAPublicKey) it else throw IllegalArgumentException("Public key does not rsa") }
}

private fun private(certificate: KeyStore, config: JWTAuthenticationConfig): RSAPrivateKey {
    return certificate.getKey(config.alias, config.password.toCharArray())
        .let { if (it is RSAPrivateKey) it else throw IllegalArgumentException("Public key does not rsa") }
}

private fun algorithm(publicKey: RSAPublicKey, privateKey: RSAPrivateKey): Algorithm {
    return Algorithm.RSA256(publicKey, privateKey)
}

private fun defaultVerifier(algorithm: Algorithm, config: JWTAuthenticationConfig): JWTVerifier {
    return JWT.require(algorithm)
        .withClaimPresence("accountId")
        .withClaimPresence("authorities")
        .withAnyOfAudience(config.audience)
        .build()
}