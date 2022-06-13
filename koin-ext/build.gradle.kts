plugins {
    kotlin("jvm") version Dependencies.Kotlin.version
}

group = Config.Application.group
version = Config.Application.version

dependencies {
    implementation(Dependencies.Ktor.core)
    api(Dependencies.Koin.core)
}