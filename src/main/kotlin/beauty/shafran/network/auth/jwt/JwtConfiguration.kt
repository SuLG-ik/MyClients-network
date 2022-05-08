package beauty.shafran.network.auth.jwt

import com.auth0.jwt.algorithms.Algorithm
import org.koin.core.annotation.Module
import org.koin.core.annotation.Single
import java.security.KeyStore
import java.security.interfaces.RSAPrivateKey
import java.security.interfaces.RSAPublicKey
import kotlin.io.path.Path
import kotlin.io.path.inputStream


@Module
class JwtConfiguration {

    @Single
    fun keyStore(jwtConfig: JwtConfig): KeyStore {
        val keyStore = KeyStore.getInstance(KeyStore.getDefaultType())
        val resourceAsStream = Path(jwtConfig.path).inputStream()
        return keyStore.apply { load(resourceAsStream, jwtConfig.storePassword.toCharArray()) }
    }

    @Single
    fun jwtSigningKey(keyStore: KeyStore, jwtConfig: JwtConfig): RSAPrivateKey {
        val key = keyStore.getKey(jwtConfig.alias, jwtConfig.password.toCharArray())
        if (key is RSAPrivateKey) {
            return key
        }
        throw IllegalArgumentException("Unable to load private key")
    }

    @Single
    fun jwtValidationKey(keyStore: KeyStore, jwtConfig: JwtConfig): RSAPublicKey {
        val certificate = keyStore.getCertificate(jwtConfig.alias)
        val publicKey = certificate?.publicKey
        if (publicKey is RSAPublicKey) {
            return publicKey
        }
        throw IllegalArgumentException("Unable to load RSA public key")
    }

    @Single
    fun algorithm(publicKey: RSAPublicKey, privateKey: RSAPrivateKey): Algorithm {
        return Algorithm.RSA256(publicKey, privateKey)
    }

}