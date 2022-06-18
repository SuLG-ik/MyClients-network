plugins {
    kotlin("jvm") version Dependencies.Kotlin.version
    kotlin("plugin.serialization") version Dependencies.Kotlin.version
}

group = "ru.myclients"
version = "0.0.1"
tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = freeCompilerArgs + "-Xcontext-receivers"
    }
}
dependencies {
    implementation(Dependencies.Ktor.core)
    implementation(Dependencies.Ktor.auth)
    implementation(Dependencies.)
    implementation(projects.exceptionsData)
}