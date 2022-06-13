plugins {
    kotlin("jvm") version Dependencies.Kotlin.version
    kotlin("plugin.serialization") version Dependencies.Kotlin.version
}

group = Config.Application.group
version = Config.Application.version

dependencies {
    implementation(projects.koinExt)
    implementation(Dependencies.Ktor.core)
    implementation(Dependencies.Ktor.contentNegotiation)
    implementation(Dependencies.Ktor.contentNegotiationJson)
    implementation(Dependencies.Serialization.core)
}