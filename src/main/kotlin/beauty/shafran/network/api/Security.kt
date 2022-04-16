package beauty.shafran.network.api

import beauty.shafran.network.auth.repository.AuthRepository
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter

@Configuration
@EnableConfigurationProperties
class Security(
    val authRepository: AuthRepository,
) : WebSecurityConfigurerAdapter() {



}