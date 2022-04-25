package beauty.shafran.network.auth.jwt

import com.nimbusds.jose.jwk.JWK
import com.nimbusds.jose.jwk.JWKSet
import com.nimbusds.jose.jwk.source.ImmutableJWKSet
import com.nimbusds.jose.jwk.source.JWKSource
import com.nimbusds.jose.proc.SecurityContext
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.oauth2.jwt.JwtEncoder
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder
import org.springframework.security.oauth2.jwt.NimbusReactiveJwtDecoder
import java.security.Key
import java.security.KeyStore
import java.security.interfaces.RSAPrivateKey
import java.security.interfaces.RSAPublicKey
import kotlin.io.path.Path
import kotlin.io.path.inputStream


@Configuration
class JwtConfiguration(
    private val jwtConfig: JwtConfig,
) {

    @Bean
    fun keyStore(): KeyStore {
        val keyStore = KeyStore.getInstance(KeyStore.getDefaultType())
        val resourceAsStream = Path(jwtConfig.path).inputStream()
        return keyStore.apply { load(resourceAsStream, jwtConfig.storePassword.toCharArray()) }
    }

    @Bean
    fun jwtSigningKey(keyStore: KeyStore): RSAPrivateKey? {
        val key: Key = keyStore.getKey(jwtConfig.alias, jwtConfig.password.toCharArray())
        if (key is RSAPrivateKey) {
            return key
        }
        throw IllegalArgumentException("Unable to load private key")
    }

    @Bean
    fun jwtValidationKey(keyStore: KeyStore): RSAPublicKey? {
        val certificate = keyStore.getCertificate(jwtConfig.alias)
        val publicKey = certificate?.publicKey
        if (publicKey is RSAPublicKey) {
            return publicKey
        }
        throw IllegalArgumentException("Unable to load RSA public key")
    }

    @Bean
    fun jwk(keyStore: KeyStore): JWK {
        return JWK.load(keyStore, jwtConfig.alias, jwtConfig.storePassword.toCharArray())
    }

    @Bean
    fun jwkSource(jwk: JWK): JWKSource<SecurityContext> {
        val jwkSet = JWKSet(jwk)
        return ImmutableJWKSet(jwkSet)
    }

    @Bean
    fun jwtDecoder(rsaPublicKey: RSAPublicKey): NimbusReactiveJwtDecoder {
        return NimbusReactiveJwtDecoder.withPublicKey(rsaPublicKey).build()
    }


    @Bean
    fun jwtEncoder(source: JWKSource<SecurityContext>): JwtEncoder {
        return NimbusJwtEncoder(source)
    }

}