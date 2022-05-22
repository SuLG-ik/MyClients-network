package beauty.shafran.network.auth

import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*

interface AuthenticationValidator : suspend ApplicationCall.(JWTCredential) -> Principal? {

    override suspend operator fun invoke(call: ApplicationCall, credentials: JWTCredential): Principal?

}