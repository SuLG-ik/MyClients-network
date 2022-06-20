interface Config {
    object Application {
        const val version = "0.0.1"
        const val group = "beauty.shafran.network"
    }

    object Compile {
        val archiveName = "network"
    }

}

interface Dependencies {

    object Spring {
        val version =   "2.7.0"
        val bom = "org.springframework.boot:spring-boot-dependencies:$version"
        val jpaStarter =   "org.springframework.boot:spring-boot-starter-data-jpa"
        val graphQlStarter = "org.springframework.boot:spring-boot-starter-graphql"
        val queryDslCore = "com.querydsl:querydsl-core"
        val queryDslJpa = "com.querydsl:querydsl-jpa"
        val queryDslKapt = "com.querydsl:querydsl-apt:5.0.0:jpa"
        val webStarter = "org.springframework.boot:spring-boot-starter-web"
        val securityStarter = "org.springframework.boot:spring-boot-starter-security"
    }
    object Shadow {
        val plugin = "com.github.johnrengelman.shadow"
        val version = "7.1.2"

    }

    object Kotlin {
        const val version = "1.7.0"
        const val reflect = "org.jetbrains.kotlin:kotlin-reflect:$version"
    }

    object Coroutines {
        const val version = "1.6.2"
        const val core = "org.jetbrains.kotlinx:kotlinx-coroutines-core:$version"
    }


    object Ktor {
        const val version = "2.0.2"
        const val core = "io.ktor:ktor-server-core:$version"
        const val netty = "io.ktor:ktor-server-netty:$version"
        const val contentNegotiation = "io.ktor:ktor-server-content-negotiation:$version"
        const val contentNegotiationJson = "io.ktor:ktor-serialization-kotlinx-json:$version"
        const val auth = "io.ktor:ktor-server-auth:$version"
        const val authJwt = "io.ktor:ktor-server-auth-jwt:$version"
        const val statusPages = "io.ktor:ktor-server-status-pages:$version"
        const val httpRedirect = "io.ktor:ktor-server-http-redirect:$version"
        const val callLogging = "io.ktor:ktor-server-call-logging:$version"
    }

    object Koin {
        const val version = "3.2.0"
        const val core = "io.insert-koin:koin-core:$version"
    }

    object Auth0 {
        const val jwt = "com.auth0:java-jwt:3.19.2"
    }


    object BCrypt {
        const val favreLib = "at.favre.lib:bcrypt:0.9.0"
        const val mindrotLib  = "org.mindrot:jbcrypt:0.4"
    }

    object Logback {
        const val version = "1.2.11"
        const val classic = "ch.qos.logback:logback-classic:$version"
    }

    object Exposed {
        const val version = "0.38.2"
        const val core = "org.jetbrains.exposed:exposed-core:$version"
        const val jdbc = "org.jetbrains.exposed:exposed-jdbc:$version"
        const val datetime = "org.jetbrains.exposed:exposed-kotlin-datetime:$version"
    }

    object Postgres {
        const val driver = "org.postgresql:postgresql:42.3.5"
        const val postgis = "net.postgis:postgis-jdbc:2021.1.0"
    }

    object Datetime {
        const val version = "0.3.2"
        const val core = "org.jetbrains.kotlinx:kotlinx-datetime:$version"
    }

    object Serialization {
        const val version = "1.3.3"
        const val json = "org.jetbrains.kotlinx:kotlinx-serialization-json:$version"
        const val core = "org.jetbrains.kotlinx:kotlinx-serialization-core:$version"
        const val jackson = "com.fasterxml.jackson.module:jackson-module-kotlin"
    }

    object KotlinPoet {
        const val version = "1.11.0"
        const val core = "com.squareup:kotlinpoet:$version"
        const val ksp = "com.squareup:kotlinpoet-ksp:$version"
    }

    object Ksp {
        const val version = "1.6.21-1.0.6"
        const val core = "com.google.devtools.ksp:symbol-processing-api:$version"
        const val plugin = "com.google.devtools.ksp"
    }

}