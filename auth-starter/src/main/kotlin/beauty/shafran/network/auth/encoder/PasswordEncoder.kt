package beauty.shafran.network.auth.encoder

interface PasswordEncoder {

    fun matches(rawPassword: String, passwordHash: String): Boolean

    fun hashPassword(rawPassword: String): String

}