package beauty.shafran.network.api

import org.springframework.context.annotation.Configuration
import org.springframework.core.annotation.Order
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.authentication.preauth.AbstractPreAuthenticatedProcessingFilter
import javax.servlet.http.HttpServletRequest


class APIKeyAuthFilter(
    private val config: ApiKeyConfig,
) : AbstractPreAuthenticatedProcessingFilter() {

    override fun getPreAuthenticatedCredentials(request: HttpServletRequest?): String {
        return ""
    }

    override fun getPreAuthenticatedPrincipal(request: HttpServletRequest?): String {
        return request?.getHeader(config.header) ?: ""
    }

}

@Configuration
@EnableWebSecurity
@Order(1)
class APISecurityConfig(
    private val config: ApiKeyConfig,
) : WebSecurityConfigurerAdapter() {

    override fun configure(httpSecurity: HttpSecurity) {
        val filter = APIKeyAuthFilter(config)
        filter.setAuthenticationManager { authentication ->
            val principal = authentication.principal
            authentication.apply { isAuthenticated = config.key == principal }
        }
        httpSecurity.antMatcher("/v1/**").csrf().disable().sessionManagement()
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS).and().addFilter(filter).authorizeRequests()
            .anyRequest().authenticated()
    }

}