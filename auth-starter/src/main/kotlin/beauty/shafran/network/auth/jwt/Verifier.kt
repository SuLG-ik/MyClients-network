package beauty.shafran.network.auth.jwt

import com.auth0.jwt.interfaces.DecodedJWT
import com.auth0.jwt.interfaces.JWTVerifier

interface Verifier : JWTVerifier {

    override fun verify(token: String): DecodedJWT

    override fun verify(jwt: DecodedJWT): DecodedJWT

    fun verifyIgnoreExpire(token: String): DecodedJWT

    fun verifyIgnoreExpire(jwt: DecodedJWT): DecodedJWT
}