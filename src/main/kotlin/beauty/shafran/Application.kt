package beauty.shafran

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication
import org.springframework.web.bind.annotation.RestController


@SpringBootApplication
@ConfigurationPropertiesScan
class NetworkApplication

@RestController
class Hello

fun main(args: Array<String>) {
    runApplication<NetworkApplication>(*args)
}