package beauty.shafran.network.auth.encoder

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.DelegatingPasswordEncoder
import org.springframework.stereotype.Component

@Component
internal class DelegatingPasswordEncoder : PasswordEncoder {

    private val encoders = DelegatingPasswordEncoder("bcrypt", mapOf("bcrypt" to BCryptPasswordEncoder()))

    override fun matches(rawPassword: String, passwordHash: String): Boolean {
        return encoders.matches(rawPassword, passwordHash)
    }

    override fun hashPassword(rawPassword: String): String {
        return encoders.encode(rawPassword)
    }

}