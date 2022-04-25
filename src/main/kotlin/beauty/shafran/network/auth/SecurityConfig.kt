package beauty.shafran.network.api

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.convert.converter.Converter
import org.springframework.security.authentication.AbstractAuthenticationToken
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity
import org.springframework.security.config.web.server.ServerHttpSecurity
import org.springframework.security.config.web.server.invoke
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoder
import org.springframework.security.web.server.SecurityWebFilterChain
import reactor.core.publisher.Mono

@Configuration
@EnableWebFluxSecurity
class SecurityConfig(
) {

    @Bean
    fun springSecurityFilterChain(
        http: ServerHttpSecurity,
        converter: Converter<Jwt, Mono<AbstractAuthenticationToken>>,
        decoder: ReactiveJwtDecoder,
    ): SecurityWebFilterChain {
        return http {
            authorizeExchange {
                authorize("/v1/auth/**", permitAll)
                authorize("/v1/accounts/**", authenticated)
                authorize("/v1/employees/**", authenticated)
                authorize("/v1/services/**", authenticated)
                authorize("/v1/sessions/**", authenticated)
                authorize(anyExchange, denyAll)
            }
            csrf { disable() }
            oauth2ResourceServer {
                jwt {
                    jwtDecoder = decoder
                    jwtAuthenticationConverter = converter
                }
            }
        }
    }


}