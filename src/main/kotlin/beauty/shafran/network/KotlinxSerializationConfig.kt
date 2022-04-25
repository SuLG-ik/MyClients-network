package beauty.shafran.network

import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.http.codec.json.KotlinSerializationJsonDecoder
import org.springframework.http.codec.json.KotlinSerializationJsonEncoder
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.RestControllerAdvice

@Configuration
@RestControllerAdvice
class KotlinxSerializationConfig {

    @Bean
    fun jsonBean(): Json {
        return Json {
            classDiscriminator = "\$type"
            ignoreUnknownKeys = true
            prettyPrint = true
            encodeDefaults = true
        }
    }

    @Bean
    fun decoderBean(json: Json): KotlinSerializationJsonDecoder {
        return KotlinSerializationJsonDecoder(json)
    }

    @Bean
    fun encoderBean(json: Json): KotlinSerializationJsonEncoder {
        return KotlinSerializationJsonEncoder(json)
    }
    @ResponseBody
    @ExceptionHandler(SerializationException::class)
    fun handleShafranNetwork(
        exception: SerializationException,
    ): ResponseEntity<ShafranNetworkException> {
        return ResponseEntity(BadRequest(), HttpStatus.BAD_REQUEST)
    }



}