val ktor_version: String by project
val kotlin_version: String by project
val logback_version: String by project

plugins {
    application
    kotlin("jvm") version "1.6.10"
    kotlin("kapt") version "1.6.10"
    kotlin("plugin.serialization") version "1.6.10"
}

group = "beauty.shafran"
version = "0.0.1"
application {
    mainClass.set("beauty.shafran.ApplicationKt")
}

repositories {
    mavenCentral()
    maven { url = uri("https://maven.pkg.jetbrains.space/public/p/ktor/eap") }
}

dependencies {
    implementation("io.ktor:ktor-server-core:$ktor_version")
    implementation("io.ktor:ktor-server-netty:$ktor_version")
    implementation("io.ktor:ktor-server-auth:$ktor_version")
    implementation("io.ktor:ktor-network-tls-certificates:$ktor_version")
    implementation("io.ktor:ktor-server-status-pages:$ktor_version")
    implementation("io.ktor:ktor-server-call-logging:$ktor_version")
    implementation("io.ktor:ktor-server-content-negotiation:$ktor_version")
    implementation("io.ktor:ktor-serialization-kotlinx-json:$ktor_version")
    implementation("ch.qos.logback:logback-classic:$logback_version")
    implementation("org.litote.kmongo:kmongo-coroutine-serialization:4.5.0")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.3.2")
    implementation("io.insert-koin:koin-core:3.2.0-beta-1")
    implementation("io.insert-koin:koin-logger-slf4j:3.2.0-beta-1")
    implementation("com.arkivanov.essenty:parcelable:0.2.2")
    testImplementation("io.ktor:ktor-server-tests:$ktor_version")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:$kotlin_version")
    implementation("org.hibernate.validator:hibernate-validator:7.0.4.Final")
    kapt("org.hibernate.validator:hibernate-validator-annotation-processor:7.0.4.Final")
}