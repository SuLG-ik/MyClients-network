package beauty.shafran.network.auth.jwt

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.interfaces.JWTVerifier
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import java.io.File
import java.security.KeyStore
import java.security.cert.Certificate
import java.security.interfaces.RSAPrivateKey
import java.security.interfaces.RSAPublicKey


@Configuration
@ConfigurationPropertiesScan
class JWTConfig {

    @Bean
    fun keystore(config: JWTAuthenticationConfig): KeyStore {
        return KeyStore.getInstance(File(config.path), config.storePassword.toCharArray())
    }

    @Bean
    fun cert(keyStore: KeyStore, config: JWTAuthenticationConfig): Certificate {
        return keyStore.getCertificate(config.alias)
    }

    @Bean
    fun publicKey(certificate: Certificate): RSAPublicKey {
        return certificate.publicKey.let { if (it is RSAPublicKey) it else throw IllegalArgumentException("Public key does not rsa") }
    }

    @Bean
    fun privateKey(certificate: KeyStore, config: JWTAuthenticationConfig): RSAPrivateKey {
        return certificate.getKey(config.alias, config.password.toCharArray())
            .let { if (it is RSAPrivateKey) it else throw IllegalArgumentException("Public key does not rsa") }
    }

    @Bean
    fun algorithm(publicKey: RSAPublicKey, privateKey: RSAPrivateKey): Algorithm {
        return Algorithm.RSA256(publicKey, privateKey)
    }

    @Bean
    @Primary
    fun defaultVerifier(algorithm: Algorithm, config: JWTConfig): JWTVerifier {
        return JWT.require(algorithm)
            .withClaimPresence("userId")
            .withClaimPresence("tokenType")
            .build()
    }


    @Bean
    @Qualifier("expired_access_token")
    fun foreverVerifier(algorithm: Algorithm): JWTVerifier {
        return JWT.require(algorithm)
            .withClaim("tokenType", "access")
            .withClaimPresence("userId")
            .withClaimPresence("sessionId")
            .acceptExpiresAt(300000000)
            .build()
    }
}