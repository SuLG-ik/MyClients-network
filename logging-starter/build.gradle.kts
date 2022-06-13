plugins {
    kotlin("jvm") version Dependencies.Kotlin.version
    kotlin("plugin.serialization") version Dependencies.Kotlin.version
    id(Dependencies.Ksp.plugin) version Dependencies.Ksp.version
}

group = Config.Application.group
version = Config.Application.version

sourceSets.main {
    java.srcDirs("build/generated/ksp/main/kotlin")
}

dependencies {
    implementation(Dependencies.Ktor.core)
    implementation(Dependencies.Ktor.callLogging)
    implementation(projects.configCore)
    ksp(projects.configProcessor)
}