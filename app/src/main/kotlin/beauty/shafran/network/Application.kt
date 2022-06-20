package beauty.shafran.network

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Configuration


@EnableConfigurationProperties
@Configuration
class AppConfig

@SpringBootApplication
class MyClientsNetwork

fun main(args: Array<String>) {
    runApplication<MyClientsNetwork>(*args)
}


