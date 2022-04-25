package beauty.shafran

import org.springframework.context.annotation.Configuration
import org.springframework.http.codec.ServerCodecConfigurer
import org.springframework.http.codec.json.KotlinSerializationJsonDecoder
import org.springframework.http.codec.json.KotlinSerializationJsonEncoder
import org.springframework.web.reactive.config.EnableWebFlux
import org.springframework.web.reactive.config.WebFluxConfigurer

@EnableWebFlux
@Configuration
class WebConfig(
    private val decoder: KotlinSerializationJsonDecoder,
    private val encoder: KotlinSerializationJsonEncoder
) : WebFluxConfigurer {

    override fun configureHttpMessageCodecs(configurer: ServerCodecConfigurer) {
        configurer.registerDefaults(true)
        configurer.customCodecs().register(decoder)
        configurer.customCodecs().register(encoder)
    }

}
