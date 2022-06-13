package beauty.shafran.network.auth.encoder

class DelegatingPasswordEncoder(
    private val primary: String,
    private val encoders: Map<String, PasswordEncoder>,
) : PasswordEncoder {

    private val primaryEncoder = encoders.getValue(primary)

    override fun matches(rawPassword: String, passwordHash: String): Boolean {
        val encoder = encoders.firstNotNullOf { if (passwordHash.startsWith("{${it.key}}")) it else null }
        return encoder.value.matches(rawPassword, passwordHash.removePrefix("{${encoder.key}}"))
    }

    override fun hashPassword(rawPassword: String): String {
        return "{${primary}}${primaryEncoder.hashPassword(rawPassword)}"
    }

}