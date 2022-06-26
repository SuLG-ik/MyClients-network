package beauty.shafran.network

import beauty.shafran.network.auth.jwt.JWTAuthenticationFilter
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
private class AuthConfiguration {


    @Bean
    fun security(http: HttpSecurity, filter: JWTAuthenticationFilter): SecurityFilterChain {
        return http
            .sessionManagement()
            .disable()
            .httpBasic()
            .disable()
            .csrf()
            .disable()
            .addFilterAt(
                filter,
                UsernamePasswordAuthenticationFilter::class.java
            )
            .authorizeRequests()
            .antMatchers("/graphql").permitAll()
            .anyRequest().denyAll()
            .and()
            .build()
    }

}
