package beauty.shafran

import kotlinx.serialization.json.Json
import org.springframework.context.annotation.Configuration
import org.springframework.http.converter.HttpMessageConverter
import org.springframework.http.converter.json.KotlinSerializationJsonHttpMessageConverter
import org.springframework.web.servlet.config.annotation.EnableWebMvc
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@EnableWebMvc
@Configuration
class WebConfig : WebMvcConfigurer {

    override fun configureMessageConverters(converters: MutableList<HttpMessageConverter<*>?>) {
        super.configureMessageConverters(converters)
        converters.add(KotlinSerializationJsonHttpMessageConverter(Json {
            classDiscriminator = "\$type"
            ignoreUnknownKeys = true
            prettyPrint = true
            encodeDefaults = true
        }))
    }

}
