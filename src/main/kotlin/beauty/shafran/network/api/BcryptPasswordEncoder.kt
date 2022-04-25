package beauty.shafran.network.api

import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.security.crypto.password.DelegatingPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

interface AuthPasswordEncoder {

    fun encode(rawPassword: String): String

    fun match(rawPassword: String, encodedPassword: String): Boolean

}


@Configuration
class EncodersConfiguration {

    @Bean
    @Primary
    fun getEncoders(
        @Qualifier("bcrypt_encoder")
        bcryptPasswordEncoder: PasswordEncoder,
    ): PasswordEncoder {
        return DelegatingPasswordEncoder("bcrypt", mapOf(
            "bcrypt" to bcryptPasswordEncoder
        ))
    }

}

@Service
@Primary
class GeneralPasswordEncoder(
    private val encoder: PasswordEncoder,
) : AuthPasswordEncoder {

    override fun encode(rawPassword: String): String {
        return encoder.encode(rawPassword)
    }

    override fun match(rawPassword: String, encodedPassword: String): Boolean {
        return encoder.matches(rawPassword, encodedPassword)
    }

}