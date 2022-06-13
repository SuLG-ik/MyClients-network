package beauty.shafran.network

import beauty.shafran.network.auth.jwt.JWTAuthentication
import beauty.shafran.network.auth.schema.createAnnotatedMissingTablesAndColumns
import beauty.shafran.network.database.SetupTransactional
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import org.jetbrains.exposed.sql.SchemaUtils

fun Application.jwtAuth() {
    val validator: JWTAuthentication = get()
    authentication {
        jwt(name = "client") {
            realm = validator.realm
            verifier(validator.verifier)
            validate(validate = validator.validator)
        }
    }
}