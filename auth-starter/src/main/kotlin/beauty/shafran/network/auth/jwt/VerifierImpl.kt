package beauty.shafran.network.auth.jwt

import beauty.shafran.network.AccessDenied
import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.exceptions.JWTVerificationException
import com.auth0.jwt.interfaces.DecodedJWT
import com.auth0.jwt.interfaces.JWTVerifier
import java.time.Instant
import java.util.Date

internal class VerifierImpl(
    algorithm: Algorithm,
    config: JWTAuthenticationConfig,
) : Verifier {


    private val defaultVerifier: JWTVerifier = JWT.require(algorithm)
        .withAnyOfAudience(config.audience)
        .withClaimPresence("accountId")
        .withClaimPresence("authorities")
        .build()

    private val ignoreVerifier: JWTVerifier = JWT.require(algorithm)
        .withAnyOfAudience(config.audience)
        .withClaimPresence("accountId")
        .withClaimPresence("authorities")
        .acceptExpiresAt(389864403199)
        .build()

    override fun verify(token: String): DecodedJWT {
        return withExceptionMapping { defaultVerifier.verify(token) }
    }

    override fun verify(jwt: DecodedJWT): DecodedJWT {
        return withExceptionMapping { defaultVerifier.verify(jwt) }
    }

    override fun verifyIgnoreExpire(token: String): DecodedJWT {
        return withExceptionMapping { ignoreVerifier.verify(token) }
    }

    override fun verifyIgnoreExpire(jwt: DecodedJWT): DecodedJWT {
        return withExceptionMapping { ignoreVerifier.verify(jwt) }
    }

    private inline fun <T> withExceptionMapping(block: () -> T): T {
        return try {
            block()
        } catch (e: JWTVerificationException) {
            throw AccessDenied()
        }
    }

}