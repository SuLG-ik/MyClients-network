package beauty.shafran

import beauty.shafran.network.auth.AuthenticationValidator
import beauty.shafran.network.auth.jwt.JwtConfig
import com.auth0.jwt.algorithms.Algorithm
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*

const val CompaniesAuthScope = "companies_scope"
const val AdminAuthScope = "admin_scope"

fun Application.jwtAuthentication() {
    val config: JwtConfig = get()
    val algorithm: Algorithm = get()
    val validator: AuthenticationValidator = get()
    authentication {
        jwt(CompaniesAuthScope) {
            realm = config.realm
            verifier(config.issuer, config.audience, algorithm) {
                acceptLeeway(5)
            }
            validate(validate = validator)
        }
    }
}

fun Application.adminAuthentication() {
    authentication {
        basic(AdminAuthScope) {
            validate { if (it.name == "admin" && it.password == "admin") UserIdPrincipal("admin") else null }
        }
    }
}