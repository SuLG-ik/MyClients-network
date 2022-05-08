package beauty.shafran.network.api

import org.koin.core.annotation.Module
import org.koin.core.annotation.Single


@Module
class PasswordModule {

    @Single
    fun generalPasswordEncoder(bCryptPasswordEncoder: BCryptPasswordEncoder): AuthPasswordEncoder {
        return GeneralPasswordEncoder(
            mapOf("bcrypt" to bCryptPasswordEncoder),
            "bcrypt"
        )
    }

}

interface AuthPasswordEncoder {

    fun encode(rawPassword: String): String

    fun match(rawPassword: String, encodedPassword: String): Boolean

}

class GeneralPasswordEncoder(
    private val encoders: Map<String, AuthPasswordEncoder>,
    private val primaryEncoder: String,
) : AuthPasswordEncoder {

    override fun encode(rawPassword: String): String {
        val hash = encoders[primaryEncoder]!!.encode(rawPassword)
        return "{${primaryEncoder}}${hash}"
    }

    override fun match(rawPassword: String, encodedPassword: String): Boolean {
        val encoder =
            encoders.firstNotNullOf { if (encodedPassword.startsWith("{${it.key}}")) it else null }
        return encoder.value.match(rawPassword, encodedPassword.removePrefix("{${encoder.key}}"))
    }

}