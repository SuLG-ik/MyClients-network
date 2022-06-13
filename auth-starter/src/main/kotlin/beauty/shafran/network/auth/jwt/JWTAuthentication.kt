package beauty.shafran.network.auth.jwt

import beauty.shafran.network.auth.AuthorizedAccountPrincipal
import com.auth0.jwt.interfaces.JWTVerifier
import io.ktor.server.application.*
import io.ktor.server.auth.jwt.*

internal interface JWTAuthentication {

    val realm: String
    val verifier: JWTVerifier
    val validator: suspend ApplicationCall.(JWTCredential) -> AuthorizedAccountPrincipal?

    val configuration: String
}