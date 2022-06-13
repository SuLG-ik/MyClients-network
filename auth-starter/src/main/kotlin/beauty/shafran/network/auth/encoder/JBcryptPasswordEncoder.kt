package beauty.shafran.network.auth.encoder

import org.mindrot.jbcrypt.BCrypt

class JBcryptPasswordEncoder : PasswordEncoder {

    override fun matches(rawPassword: String, passwordHash: String): Boolean {
        return BCrypt.checkpw(rawPassword, passwordHash)
    }

    override fun hashPassword(rawPassword: String): String {
        val salt = BCrypt.gensalt()
        return BCrypt.hashpw(rawPassword, salt)
    }

}