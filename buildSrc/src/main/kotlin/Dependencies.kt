interface Dependencies {

    object Kotlin {
        val version = "1.7.0-RC"
    }

    object Ktor {
        val version = "2.0.2"
        var core = "io.ktor:ktor-server-core:$version"
        var netty = "io.ktor:ktor-server-netty:$version"
        var contentNegotiation = "io.ktor:ktor-server-content-negotiation:$version"
        val json = "io.ktor:ktor-serialization-kotlinx-json:$version"
        var auth = "io.ktor:ktor-server-auth:$version"
        var authJwt = "io.ktor:ktor-server-auth-jwt:$version"
        var statusPages = "io.ktor:ktor-server-status-pages:$version"
        var httpRedirect = "io.ktor:ktor-server-http-redirect:$version"
        var callLogging = "io.ktor:ktor-server-call-logging:$version"
    }

    object Koin {
        val version = "3.2.0"

        val core = "io.insert-koin:koin-core:$version"
    }

    object BCrypt {
        val favreLib = "at.favre.lib:bcrypt:0.9.0"
    }

    object Logback {
        val version = "1.2.11"
        val classic = "ch.qos.logback:logback-classic:$version"
    }

    object Exposed {
        val version = "0.38.2"
        val core = "org.jetbrains.exposed:exposed-core:$version"
        val jdbc = "org.jetbrains.exposed:exposed-jdbc:$version"
        val datetime = "org.jetbrains.exposed:exposed-kotlin-datetime:$version"
    }

    object Postgres {
        val driver = "org.postgresql:postgresql:42.3.5"
        val postgis = "net.postgis:postgis-jdbc:2021.1.0"
    }

    object Serialization {
        val version = "1.3.3"
        val json = "org.jetbrains.kotlinx:kotlinx-serialization-json:$version"
        val core = "org.jetbrains.kotlinx:kotlinx-serialization-core:$version"
    }

}