package beauty.shafran.network

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Configuration
import org.springframework.data.jpa.repository.config.EnableJpaAuditing
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.springframework.transaction.annotation.EnableTransactionManagement


@EnableConfigurationProperties
@EnableTransactionManagement
@EnableJpaRepositories
@EnableJpaAuditing
@Configuration
class AppConfig

@SpringBootApplication
class MyClientsNetwork

fun main(args: Array<String>) {
    runApplication<MyClientsNetwork>(*args)
}


