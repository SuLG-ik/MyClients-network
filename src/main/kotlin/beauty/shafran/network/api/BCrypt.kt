package beauty.shafran.network.api

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder

@Configuration
class BCrypt {

    @Bean("bcrypt_encoder")
    fun bCryptEncoderBean(): PasswordEncoder {
        return BCryptPasswordEncoder()
    }

}