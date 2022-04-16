package beauty.shafran.network.api

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder

@Configuration
class BCrypt {

    @Bean
    fun bCryptEncoderBean(): BCryptPasswordEncoder {
        return BCryptPasswordEncoder()
    }

}